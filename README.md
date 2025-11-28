# SecureTrust Bank — Professional Banking System

The most beautiful, functional banking web app you'll ever run in Codespaces.

[![Open in GitHub Codespaces](https://github.com/codespaces/badge.svg)](https://codespaces.new/Locx26/SecureTrust-Bank)

## Features
- Stunning glassmorphism login
- Full dashboard with Chart.js
- Real customers & accounts
- Complete banking operations (Deposit, Withdraw, Transfer)
- Transaction history tracking
- Customer management
- 100% works in GitHub Codespaces
- Zero setup

## How to Run

### Option 1: GitHub Codespaces (Easiest)
1. Click the badge above or go to your repository
2. Click the green "Code" button
3. Select "Codespaces" tab
4. Click "Create codespace on main"
5. Wait for the environment to set up (~30 seconds)
6. The application will compile automatically
7. Run: `cd backend && mvn spring-boot:run`
8. Open the application at `http://localhost:8080`

### Option 2: Local Development

**Prerequisites:**
- Java 17 or higher
- Maven 3.6+

**Steps:**
```bash
# 1. Clone the repository
git clone https://github.com/Locx26/ddd.git
cd ddd/backend

# 2. Build the application
mvn clean package

# 3. Run the application
java -jar target/bank-web-1.0.0.jar

# OR use Maven directly
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

### Option 3: Quick Run (If already built)
```bash
cd backend
mvn spring-boot:run
```

## Login Credentials

### Administrator Login
- **Email:** `admin@securetrust.com`  
- **Password:** `Admin123!`

### Customer Login
Customers can login to view their own accounts and transactions.
- **Email:** `john.doe@securetrust.com`  
- **Password:** `password123`

(Other customers: `jane.smith@securetrust.com`, `bob.johnson@securetrust.com`, `finance@techcorp.co.bw` - all with password `password123`)

## What You Can Do

### As Administrator
✅ **View Dashboard** - See account statistics and charts  
✅ **Manage Customers** - View customer profiles and accounts  
✅ **Deposit Funds** - Add money to any account  
✅ **Withdraw Funds** - Remove money from accounts (with balance validation)  
✅ **Transfer Money** - Transfer between accounts  
✅ **View Transactions** - Complete transaction history with audit trail  
✅ **Notifications** - View and manage notifications  
✅ **Settings** - Manage account preferences and security  

### As Customer
✅ **View Customer Portal** - See personal account balances and transactions  
✅ **Notifications** - View notifications  
✅ **Settings** - Change password and manage account preferences  

## Application URLs
- **Login:** `http://localhost:8080/login`
- **Dashboard (Admin):** `http://localhost:8080/dashboard`
- **Customer Portal:** `http://localhost:8080/customer-portal`
- **Customers:** `http://localhost:8080/customers`
- **Transactions:** `http://localhost:8080/transactions`
- **Notifications:** `http://localhost:8080/notifications`
- **Settings:** `http://localhost:8080/settings`

## Troubleshooting

**Port already in use?**
```bash
# Find and kill the process
lsof -i :8080
kill -9 <PID>
```

**Build issues?**
```bash
# Clean and rebuild
mvn clean install -U
```

**Database issues?**
```bash
# Remove the database file and restart
rm -rf backend/data/
```

## Tech Stack
- **Backend:** Spring Boot 3.2.5, Java 17
- **Database:** H2 (embedded, file-based)
- **Frontend:** Thymeleaf, Tailwind CSS, Chart.js
- **Build Tool:** Maven

## Removing Copilot as Co-Author

This repository is configured to prevent GitHub Copilot from being added as a co-author in commit messages. The shared VS Code settings (`.vscode/settings.json`) disable Copilot suggestions in the commit message input field.

### For VS Code Users
The repository's `.vscode/settings.json` already includes the setting to disable Copilot co-authoring:
```json
{
  "github.copilot.enable": {
    "scminput": false
  }
}
```

### For Other IDEs
If you're using a different IDE (JetBrains, etc.), check your IDE's Copilot settings and disable commit message suggestions.

### Manual Removal
If you see "Co-authored-by: Copilot" in your commit messages, you can:
1. Edit the commit message before committing to remove the co-author line
2. Use `git commit --amend` to edit the most recent commit message (before pushing)
