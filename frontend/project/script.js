/* ================================================================
   MICRO-CHP Energy Management System
   Vanilla JavaScript — communicates with Spring Boot backend
   ================================================================ */

const API_BASE_URL = "http://localhost:8080/api/chp";

/* ================================================================
   LOGIN PAGE LOGIC
   ================================================================ */

function initLogin() {
  const loginForm = document.getElementById("loginForm");
  if (!loginForm) return;

  // If already logged in, go to dashboard
  if (sessionStorage.getItem("loggedIn") === "true") {
    window.location.href = "index.html";
    return;
  }

  loginForm.addEventListener("submit", function (e) {
    e.preventDefault();
    const username = document.getElementById("username").value.trim();
    const password = document.getElementById("password").value;
    const errorEl  = document.getElementById("loginError");

    if (username === "admin" && password === "admin123") {
      sessionStorage.setItem("loggedIn", "true");
      window.location.href = "index.html";
    } else {
      errorEl.style.display = "block";
      setTimeout(() => { errorEl.style.display = "none"; }, 4000);
    }
  });
}

/* ================================================================
   DASHBOARD / APP PAGE LOGIC
   ================================================================ */

function initApp() {
  // Guard: redirect if not logged in
  if (sessionStorage.getItem("loggedIn") !== "true") {
    window.location.href = "login.html";
    return;
  }

  setupNavigation();
  setupLogout();
  setupForms();
  setupMenuToggle();

  // Load dashboard data on start
  loadDashboard();
  checkBackendConnection();
}

/* ================================================================
   NAVIGATION
   ================================================================ */

const SECTION_TITLES = {
  dashboard:  "Dashboard",
  units:      "CHP Units",
  contacts:   "Contacts",
  telemetry:  "Telemetry",
  purchases:  "Purchases",
  invoices:   "Invoices",
  accounting: "Accounting",
  budget:     "Budget",
  reports:    "Reports",
};

function setupNavigation() {
  const navButtons = document.querySelectorAll(".nav-btn[data-section]");
  navButtons.forEach(function (btn) {
    btn.addEventListener("click", function () {
      const section = btn.getAttribute("data-section");
      showSection(section);
    });
  });
}

function showSection(section) {
  // Hide all sections
  document.querySelectorAll(".page-section").forEach(function (s) {
    s.classList.remove("active");
  });

  // Show target
  const target = document.getElementById("section-" + section);
  if (target) target.classList.add("active");

  // Update nav active state
  document.querySelectorAll(".nav-btn").forEach(function (b) {
    b.classList.remove("active");
  });
  const activeBtn = document.querySelector('.nav-btn[data-section="' + section + '"]');
  if (activeBtn) activeBtn.classList.add("active");

  // Update title
  document.getElementById("pageTitle").textContent = SECTION_TITLES[section] || section;

  // Auto-load data when entering a section
  if (section === "dashboard") {
    loadDashboard();
  } else if (section !== "reports") {
    loadTable(section);
  }

  // Close mobile sidebar
  document.getElementById("sidebar").classList.remove("open");
}

/* ================================================================
   LOGOUT
   ================================================================ */

function setupLogout() {
  const logoutBtn = document.getElementById("logoutBtn");
  if (!logoutBtn) return;
  logoutBtn.addEventListener("click", function () {
    sessionStorage.removeItem("loggedIn");
    window.location.href = "login.html";
  });
}

/* ================================================================
   MOBILE MENU TOGGLE
   ================================================================ */

function setupMenuToggle() {
  const toggle = document.getElementById("menuToggle");
  if (!toggle) return;
  toggle.addEventListener("click", function () {
    document.getElementById("sidebar").classList.toggle("open");
  });
}

/* ================================================================
   FORM SETUP — wire submit handlers for every entity form
   ================================================================ */

function setupForms() {
  const formConfig = [
    { id: "unitForm",      endpoint: "/units",     msgId: "unitMsg",      table: "units",     clearOnSuccess: true },
    { id: "contactForm",   endpoint: "/contacts",  msgId: "contactMsg",   table: "contacts",  clearOnSuccess: true },
    { id: "telemetryForm", endpoint: "/telemetry", msgId: "telemetryMsg", table: "telemetry", clearOnSuccess: true },
    { id: "purchaseForm",  endpoint: "/purchases", msgId: "purchaseMsg",  table: "purchases", clearOnSuccess: true },
    { id: "invoiceForm",   endpoint: "/invoices",  msgId: "invoiceMsg",   table: "invoices",  clearOnSuccess: true },
    { id: "accountForm",   endpoint: "/accounts",  msgId: "accountMsg",   table: "accounts",  clearOnSuccess: true },
    { id: "budgetForm",    endpoint: "/budgets",   msgId: "budgetMsg",    table: "budgets",   clearOnSuccess: true },
  ];

  formConfig.forEach(function (cfg) {
    const form = document.getElementById(cfg.id);
    if (!form) return;
    form.addEventListener("submit", function (e) {
      e.preventDefault();
      submitForm(cfg);
    });
  });
}

/* ================================================================
   GENERIC FORM SUBMIT
   ================================================================ */

async function submitForm(cfg) {
  const form = document.getElementById(cfg.id);
  const msgEl = document.getElementById(cfg.msgId);
  const submitBtn = form.querySelector('button[type="submit"]');

  // Build JSON from form fields
  const payload = buildPayload(form);

  // Show loading on button
  const originalText = submitBtn.textContent;
  submitBtn.textContent = "Saving...";
  submitBtn.disabled = true;
  msgEl.textContent = "";
  msgEl.className = "form-msg";

  try {
    const response = await fetch(API_BASE_URL + cfg.endpoint, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(payload),
    });

    if (!response.ok) {
      const errorText = await response.text();
      throw new Error(errorText || "Request failed");
    }

    // Success
    msgEl.textContent = "Saved successfully.";
    msgEl.classList.add("success");
    showToast("Saved successfully.", "success");

    if (cfg.clearOnSuccess) clearForm(cfg.id);
    loadTable(cfg.table);
    loadDashboard(); // refresh dashboard counters
  } catch (err) {
    const message = err.message || "Request failed";
    if (isConnectionError(err)) {
      msgEl.textContent = "Cannot connect to backend. Please check that Spring Boot is running on port 8080.";
    } else {
      msgEl.textContent = "Failed to add. " + message;
    }
    msgEl.classList.add("error");
    showToast(msgEl.textContent, "error");
  } finally {
    submitBtn.textContent = originalText;
    submitBtn.disabled = false;
  }
}

/* ================================================================
   BUILD JSON PAYLOAD FROM FORM
   Converts form fields to correctly-typed JSON matching backend entities
   ================================================================ */

function buildPayload(form) {
  const data = {};
  const formData = new FormData(form);

  // Type maps: which fields are numbers vs strings
  const numberFields = [
    "electricityCapacity", "heatCapacity", "heatToPowerRatio",
    "electricityTariff", "heatTariff",
    "chpUnitId", "electricityKwh", "heatGj", "hydrogenUsedKg",
    "supplierId", "hydrogenQuantityKg", "pricePerKg", "totalAmount",
    "customerId", "electricityRate", "heatRate",
    "electricityAmount", "heatAmount",
    "accountCode", "debit", "credit",
    "plannedHydrogenCost", "actualHydrogenCost",
    "plannedRevenue", "actualRevenue", "variance",
  ];

  for (const [key, value] of formData.entries()) {
    if (numberFields.includes(key)) {
      data[key] = value === "" ? 0 : parseFloat(value);
    } else {
      data[key] = value;
    }
  }

  // Convert datetime-local to ISO format for readingTime
  if (data.readingTime) {
    // datetime-local gives "2026-09-26T10:00", backend expects "2026-09-26T10:00:00"
    if (data.readingTime.length === 16) {
      data.readingTime = data.readingTime + ":00";
    }
  }

  return data;
}

/* ================================================================
   CLEAR FORM
   ================================================================ */

function clearForm(formId) {
  const form = document.getElementById(formId);
  if (!form) return;
  form.reset();
  const msgId = formId.replace("Form", "Msg");
  const msgEl = document.getElementById(msgId);
  if (msgEl) { msgEl.textContent = ""; msgEl.className = "form-msg"; }
}

/* ================================================================
   LOAD TABLE DATA
   ================================================================ */

const TABLE_CONFIG = {
  units: {
    endpoint: "/units",
    tableId: "unitsTable",
    columns: ["id", "unitName", "location", "electricityCapacity", "heatCapacity", "heatToPowerRatio", "electricityTariff", "heatTariff", "status"],
    badges: { status: true },
  },
  contacts: {
    endpoint: "/contacts",
    tableId: "contactsTable",
    columns: ["id", "name", "type", "address", "phone", "email"],
    badges: { type: true },
  },
  telemetry: {
    endpoint: "/telemetry",
    tableId: "telemetryTable",
    columns: ["id", "chpUnitId", "electricityKwh", "heatGj", "hydrogenUsedKg", "readingTime"],
    badges: {},
  },
  purchases: {
    endpoint: "/purchases",
    tableId: "purchasesTable",
    columns: ["id", "supplierId", "purchaseOrderNumber", "hydrogenQuantityKg", "pricePerKg", "totalAmount", "vendorBillNumber", "paymentStatus", "purchaseDate"],
    badges: { paymentStatus: true },
  },
  invoices: {
    endpoint: "/invoices",
    tableId: "invoicesTable",
    columns: ["id", "customerId", "electricityKwh", "heatGj", "electricityRate", "heatRate", "electricityAmount", "heatAmount", "totalAmount", "paymentStatus", "invoiceDate"],
    badges: { paymentStatus: true },
  },
  accounts: {
    endpoint: "/accounts",
    tableId: "accountsTable",
    columns: ["id", "accountCode", "accountName", "accountType", "debit", "credit", "journalType", "reference"],
    badges: { accountType: true },
    totals: { debit: "Total Debit", credit: "Total Credit" },
  },
  budgets: {
    endpoint: "/budgets",
    tableId: "budgetsTable",
    columns: ["id", "chpUnitId", "analyticAccount", "plannedHydrogenCost", "actualHydrogenCost", "plannedRevenue", "actualRevenue", "variance"],
    badges: {},
  },
};

async function loadTable(key) {
  const cfg = TABLE_CONFIG[key];
  if (!cfg) return;

  const table = document.getElementById(cfg.tableId);
  if (!table) return;

  const tbody = table.querySelector("tbody");
  tbody.innerHTML = '<tr class="table-loading"><td colspan="' + cfg.columns.length + '">Loading...</td></tr>';

  try {
    const response = await fetch(API_BASE_URL + cfg.endpoint);
    if (!response.ok) {
      const errText = await response.text();
      throw new Error(errText || "Request failed");
    }
    const data = await response.json();
    renderTable(cfg, data);
    updateBackendStatus(true);
  } catch (err) {
    if (isConnectionError(err)) {
      tbody.innerHTML = '<tr class="table-empty"><td colspan="' + cfg.columns.length +
        '">Cannot connect to backend. Please check that Spring Boot is running on port 8080.</td></tr>';
    } else {
      tbody.innerHTML = '<tr class="table-empty"><td colspan="' + cfg.columns.length +
        '">Error: ' + escapeHtml(err.message) + "</td></tr>";
    }
    updateBackendStatus(false);
  }
}

/* ================================================================
   RENDER TABLE
   ================================================================ */

function renderTable(cfg, data) {
  const table = document.getElementById(cfg.tableId);
  const tbody = table.querySelector("tbody");

  if (!data || data.length === 0) {
    tbody.innerHTML = '<tr class="table-empty"><td colspan="' + cfg.columns.length + '">No records found.</td></tr>';
    if (cfg.totals) renderTotals(cfg, []);
    return;
  }

  let html = "";
  data.forEach(function (row) {
    html += "<tr>";
    cfg.columns.forEach(function (col) {
      let value = row[col];
      // Format null/undefined
      if (value === null || value === undefined) {
        value = "";
      }
      // Badge columns
      if (cfg.badges && cfg.badges[col]) {
        html += '<td>' + statusBadge(value) + "</td>";
      } else if (typeof value === "number") {
        html += '<td>' + formatNumber(value) + "</td>";
      } else {
        html += '<td>' + escapeHtml(String(value)) + "</td>";
      }
    });
    html += "</tr>";
  });
  tbody.innerHTML = html;

  if (cfg.totals) renderTotals(cfg, data);
}

/* ================================================================
   TOTALS (for accounting)
   ================================================================ */

function renderTotals(cfg, data) {
  const totalsEl = document.getElementById("accountTotals");
  if (!totalsEl) return;

  let totals = {};
  Object.keys(cfg.totals).forEach(function (field) { totals[field] = 0; });

  data.forEach(function (row) {
    Object.keys(cfg.totals).forEach(function (field) {
      totals[field] += parseFloat(row[field]) || 0;
    });
  });

  let html = "";
  Object.keys(cfg.totals).forEach(function (field) {
    html += '<span><span class="totals-label">' + cfg.totals[field] + ':</span>' + formatNumber(totals[field]) + '</span>';
  });
  totalsEl.innerHTML = html;
}

/* ================================================================
   DASHBOARD
   ================================================================ */

async function loadDashboard() {
  setDash("dashUnits", "...");
  setDash("dashContacts", "...");
  setDash("dashTelemetry", "...");
  setDash("dashPurchases", "...");
  setDash("dashInvoices", "...");
  setDash("dashRevenue", "...");
  setDash("dashCost", "...");

  const dashConfig = [
    { key: "units",     endpoint: "/units",     countEl: "dashUnits" },
    { key: "contacts",  endpoint: "/contacts",  countEl: "dashContacts" },
    { key: "telemetry", endpoint: "/telemetry", countEl: "dashTelemetry" },
    { key: "purchases", endpoint: "/purchases", countEl: "dashPurchases", sumField: "totalAmount", sumEl: "dashCost" },
    { key: "invoices",  endpoint: "/invoices",  countEl: "dashInvoices", sumField: "totalAmount", sumEl: "dashRevenue" },
  ];

  let anySuccess = false;
  let anyFailure = false;

  await Promise.all(dashConfig.map(async function (cfg) {
    try {
      const response = await fetch(API_BASE_URL + cfg.endpoint);
      if (!response.ok) throw new Error("HTTP " + response.status);
      const data = await response.json();
      setDash(cfg.countEl, (data || []).length);
      if (cfg.sumField && cfg.sumEl) {
        const sum = (data || []).reduce(function (acc, row) {
          return acc + (parseFloat(row[cfg.sumField]) || 0);
        }, 0);
        setDash(cfg.sumEl, formatCurrency(sum));
      }
      anySuccess = true;
    } catch (err) {
      setDash(cfg.countEl, "—");
      if (cfg.sumEl) setDash(cfg.sumEl, "—");
      anyFailure = true;
    }
  }));

  updateBackendStatus(anySuccess && !anyFailure);
}

function setDash(elId, value) {
  const el = document.getElementById(elId);
  if (el) el.textContent = value;
}

/* ================================================================
   REPORTS — plain text endpoints
   ================================================================ */

const REPORT_CONFIG = {
  pnl:           { endpoint: "/reports/pnl",           outputId: "reportPnl" },
  "balance-sheet": { endpoint: "/reports/balance-sheet", outputId: "reportBalanceSheet" },
  budget:        { endpoint: "/reports/budget",        outputId: "reportBudget" },
};

async function loadReport(key) {
  const cfg = REPORT_CONFIG[key];
  if (!cfg) return;

  const output = document.getElementById(cfg.outputId);
  if (!output) return;

  output.textContent = "Loading report...";

  try {
    const response = await fetch(API_BASE_URL + cfg.endpoint);
    if (!response.ok) {
      const errText = await response.text();
      throw new Error(errText || "Request failed");
    }
    const text = await response.text();
    output.textContent = text || "Report is empty.";
    updateBackendStatus(true);
    showToast("Report loaded.", "success");
  } catch (err) {
    if (isConnectionError(err)) {
      output.textContent = "Cannot connect to backend. Please check that Spring Boot is running on port 8080.";
    } else {
      output.textContent = "Error loading report: " + err.message;
    }
    updateBackendStatus(false);
    showToast("Failed to load report.", "error");
  }
}

function loadAllReports() {
  loadReport("pnl");
  loadReport("balance-sheet");
  loadReport("budget");
}

/* ================================================================
   CALCULATE HELPERS
   ================================================================ */

function calcPurchase() {
  const form = document.getElementById("purchaseForm");
  if (!form) return;
  const qty   = parseFloat(form.elements["hydrogenQuantityKg"].value) || 0;
  const price = parseFloat(form.elements["pricePerKg"].value) || 0;
  form.elements["totalAmount"].value = (qty * price).toFixed(2);
}

function calcInvoice() {
  const form = document.getElementById("invoiceForm");
  if (!form) return;
  const elecKwh  = parseFloat(form.elements["electricityKwh"].value) || 0;
  const elecRate = parseFloat(form.elements["electricityRate"].value) || 0;
  const heatGj   = parseFloat(form.elements["heatGj"].value) || 0;
  const heatRate = parseFloat(form.elements["heatRate"].value) || 0;

  const elecAmount = elecKwh * elecRate;
  const heatAmount = heatGj * heatRate;
  const total      = elecAmount + heatAmount;

  form.elements["electricityAmount"].value = elecAmount.toFixed(2);
  form.elements["heatAmount"].value        = heatAmount.toFixed(2);
  form.elements["totalAmount"].value       = total.toFixed(2);
}

/* ================================================================
   BACKEND STATUS INDICATOR
   ================================================================ */

function updateBackendStatus(online) {
  const statusEl = document.getElementById("backendStatus");
  const textEl   = document.getElementById("statusText");
  if (!statusEl) return;

  statusEl.classList.remove("online", "offline");
  if (online) {
    statusEl.classList.add("online");
    textEl.textContent = "Backend connected";
  } else {
    statusEl.classList.add("offline");
    textEl.textContent = "Backend offline";
  }
}

async function checkBackendConnection() {
  try {
    const response = await fetch(API_BASE_URL + "/units");
    updateBackendStatus(response.ok);
  } catch (err) {
    updateBackendStatus(false);
  }
}

/* ================================================================
   UTILITIES
   ================================================================ */

function isConnectionError(err) {
  if (err instanceof TypeError && err.message.includes("fetch")) return true;
  if (err.message && err.message.toLowerCase().includes("failed to fetch")) return true;
  return false;
}

function escapeHtml(str) {
  if (!str) return "";
  const div = document.createElement("div");
  div.textContent = str;
  return div.innerHTML;
}

function formatNumber(num) {
  if (num === null || num === undefined || num === "") return "";
  const n = parseFloat(num);
  if (isNaN(n)) return String(num);
  // Show integers without decimals, floats with up to 2 decimal places
  if (Number.isInteger(n)) return n.toLocaleString();
  return n.toLocaleString(undefined, { minimumFractionDigits: 0, maximumFractionDigits: 2 });
}

function formatCurrency(num) {
  if (num === null || num === undefined || isNaN(num)) return "—";
  return "₹" + num.toLocaleString(undefined, { minimumFractionDigits: 0, maximumFractionDigits: 2 });
}

function statusBadge(value) {
  if (!value) return "";
  const v = String(value).toLowerCase();
  const text = escapeHtml(String(value));
  return '<span class="status-badge ' + v + '">' + text + '</span>';
}

/* ================================================================
   TOAST NOTIFICATIONS
   ================================================================ */

let toastTimer = null;

function showToast(message, type) {
  const toast = document.getElementById("toast");
  if (!toast) return;
  toast.textContent = message;
  toast.className = "toast show";
  if (type) toast.classList.add(type);

  if (toastTimer) clearTimeout(toastTimer);
  toastTimer = setTimeout(function () {
    toast.className = "toast";
  }, 3500);
}

/* ================================================================
   INITIALIZATION — runs on every page load
   ================================================================ */

document.addEventListener("DOMContentLoaded", function () {
  // Login page has loginForm
  if (document.getElementById("loginForm")) {
    initLogin();
  } else {
    initApp();
  }
});
