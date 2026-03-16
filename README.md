# ParentLock

A complete Android parental control system that locks WhatsApp using an AccessibilityService and TOTP-style code system.

## HOW IT WORKS:
- Child opens WhatsApp → blocked automatically → lock screen appears
- Lock screen shows a 4-digit code that changes every 120 seconds
- Child tells parent the 4 digits
- Parent enters digits into a web tool → gets a 6-digit unlock code
- Child enters 6-digit code → WhatsApp unlocks

## Features
- TOTP-based locking mechanism (120-second period)
- AccessibilityService integration for WhatsApp monitoring
- Secure code generation using HmacSHA256
- Dark-themed UI
- Setup wizard on first launch
- Persistent lock state

## Getting Started
1. Clone the repository
2. Open in Android Studio
3. Build and run the app
4. Complete the setup wizard to generate your secret key
5. Enable AccessibilityService permissions