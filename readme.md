# 📱 Discount Calculator

**A bilingual Android app that helps you calculate and compare different discount options to get the best deal — fast and easy.**

Discount Calculator App

---

## 🚀 Features

- 🔢 **Easy Discount Calculation** – Quickly calculate final prices after applying discounts.
- ⚖️ **Smart Comparison** – Compare two different discounts to find the better deal.
- 🔁 **Support for Multiple Discount Types** – Choose between percentage-based or fixed-amount discounts.
- 🌐 **Bilingual Interface** – Fully localized for English and Chinese users.
- 🎨 **Modern UI** – Clean, intuitive layout following Material Design principles.

---

## 🛠️ Tech Stack

### 🔧 Languages & Frameworks
- Java
- XML
- Android SDK
- Material Design Components

### 🧩 Architecture Highlights

#### 🌍 Language Manager System
A built-in language management feature allows seamless switching between English and Chinese:

- `LanguageManager`: Handles language preference storage and application logic.
- `BaseActivity`: Abstract class ensuring language consistency across the app.
- `LanguageSelectionActivity`: Lets users easily change the language at runtime.

#### 💸 Discount Logic

Supports two types of discounts:
- **Percentage-based** (`% off` / 折扣)
- **Fixed amount** (`$ off` / 直接減價)

Calculations are dynamically localized depending on language and discount type.

---

## ⚙️ Getting Started

### 📋 Prerequisites
- Android Studio 4.0+
- Android SDK 21+
- JDK 8+

### 🚀 Installation

```bash
# 1. Clone the repo
git clone https://github.com/mricardo888/DiscountCalculator.git

# 2. Open in Android Studio

# 3. Build the project
./gradlew build

# 4. Run on a device or emulator
./gradlew installDebug
```

---

## 🧪 How to Use

1. Enter the original item price.
2. Select the discount type for **Option A** and **Option B**.
3. Input the discount values.
4. Tap **"Calculate"**.
5. Instantly view:
   - Final price for each option
   - Total savings
   - Savings percentage
   - The better deal

---

### 🌐 Changing Language

1. Tap the language icon in the app bar.
2. Choose English or Chinese.
3. Tap **"Apply"** / **"應用"** to confirm.
4. The app will restart with your preferred language.

---

## 🧠 Developer Notes

- Locale handled correctly for Android N+.
- `BaseActivity` ensures app-wide language consistency.
- Multilingual support via dedicated string resource files.
- Language preferences are stored using `SharedPreferences`.

---

## ✅ Best Practices Followed

- Clear separation of concerns via modular classes.
- Locale updates trigger proper activity recreation.
- Responsive UI with configuration change support.
- Complete multilingual implementation.

---

## 🤝 Contributing

Want to help make this app better? Here’s how:

1. Fork the repo
2. Create your branch (`git checkout -b feature/awesome-idea`)
3. Commit your changes (`git commit -m 'Add feature: awesome idea'`)
4. Push the branch (`git push origin feature/awesome-idea`)
5. Open a Pull Request

---

## 📄 License

Licensed under the MIT License. See the [LICENSE](LICENSE) file for details.

---

## 📬 Contact

GitHub: [https://github.com/mricardo888/DiscountCalculator](https://github.com/mricardo888/DiscountCalculator)
```

Let me know if you want it tweaked for a specific GitHub theme or if you'd like badges or project stats added!