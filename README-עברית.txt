קריפטו טייקון – פרויקט Android Studio עם Google AdMob Rewarded Ads
================================================================

מה כלול?
--------
- משחק HTML המקורי בתוך app/src/main/assets/index.html
- מעטפת Android WebView
- Google Mobile Ads SDK לאנדרואיד
- פרסומת Rewarded אמיתית (בזמן פיתוח מוגדרים מזהי TEST הרשמיים של Google)
- הבונוס במשחק ניתן רק לאחר שה-SDK מודיע שהמשתמש הרוויח את התגמול
- אחרי סגירת פרסומת נטענת פרסומת נוספת

חשוב: הפרויקט מוכן לפתיחה ב-Android Studio, אך עדיין לא מוגדר לחשבון AdMob האישי שלך. קובצי Gradle Wrapper אינם כלולים; Android Studio ינהל את הורדת Gradle בעת פתיחת הפרויקט.
אין להכניס מזהי פרסום אמיתיים לפני שפתחת אפליקציה ויחידת Rewarded בחשבון AdMob.
מזהי TEST נמצאים כרגע בפרויקט כדי למנוע תנועה/קליקים לא תקינים בזמן הפיתוח.

דרישות
------
- Windows 10/11
- Android Studio עדכני
- Android SDK Platform 36
- חיבור אינטרנט להורדת Gradle והספריות

פתיחה ובנייה
------------
1. חלץ את ZIP לתיקייה במחשב.
2. פתח Android Studio.
3. בחר Open ובחר את התיקייה crypto-tycoon-android (התיקייה שבה נמצא settings.gradle).
4. אשר Trust אם Android Studio מבקש, והמתן ל-Gradle Sync.
5. חבר טלפון Android עם USB debugging או הפעל Emulator.
6. לחץ Run כדי להתקין ולבדוק.

יצירת APK לבדיקה
----------------
Build > Build Bundle(s) / APK(s) > Build APK(s)
ה-APK יופיע בדרך כלל ב:
app/build/outputs/apk/debug/app-debug.apk

הגדרת AdMob לפרסום אמיתי
------------------------
1. היכנס אל https://admob.google.com/ והוסף אפליקציית Android.
2. צור יחידת מודעה מסוג Rewarded.
3. העתק את AdMob App ID (בפורמט ca-app-pub-...~...) ואת Rewarded Ad Unit ID (בפורמט ca-app-pub-.../...).
4. פתח app/src/main/AndroidManifest.xml והחלף את הערך של:
   com.google.android.gms.ads.APPLICATION_ID
   ב-App ID שלך.
5. פתח app/src/main/java/com/example/cryptotycoon/MainActivity.java והחלף את:
   REWARDED_AD_UNIT_ID
   ב-Rewarded Ad Unit ID שלך.
6. שמור, בצע Sync/Run ובדוק תחילה עם מזהי TEST בלבד.
7. לפני הפצה, ודא שהפרטים בחשבון AdMob, מדיניות הפרטיות והגדרות ההסכמה/פרטיות מתאימים למדינות שבהן האפליקציה מופצת.

אזהרות חשובות
-------------
- אין ללחוץ על מודעות אמיתיות של עצמך. בזמן פיתוח השתמש רק במזהי TEST.
- "ללא הגבלה" מתאר שאין מגבלת פעם ביום בקוד המשחק; Google/שותפי הפרסום עדיין עשויים לא להחזיר מודעה בכל בקשה, ולכן לא מובטח שתמיד יהיה סרטון זמין.
- תגמול ניתן רק באירוע OnUserEarnedReward של Google. סגירת מודעה לפני השלמת התנאי לא נותנת בונוס.
- פרויקט זה משתמש ב-Google Mobile Ads SDK (Legacy) 25.5.0 כדי לספק מימוש Android Java מוכר. Google הכריזה ביולי 2026 על GMA Next-Gen כ-SDK המועדף לפיתוח חדש. לפני פרסום מסחרי ארוך טווח מומלץ לתכנן מעבר ל-Next-Gen ולבדוק את הוראות ההגירה העדכניות:
  https://developers.google.com/admob/android/migration
- לא נבנה כאן APK חתום ולא הוגדרו מזהי AdMob אישיים, כי אלה דורשים את חשבונך ואת מפתח החתימה שלך.

קישורים רשמיים
---------------
התחלה עם AdMob:
https://developers.google.com/admob/android/quick-start
Rewarded ads:
https://developers.google.com/admob/android/rewarded
יצירת APK חתום:
https://developer.android.com/studio/publish/app-signing
