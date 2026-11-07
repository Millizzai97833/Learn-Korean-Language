Learn Korean Language — Start.io integration correction

Replace the four files in this ZIP at the exact paths shown.

Changes:
1. Upgrade com.startapp:inapp-sdk from 5.3.1 to 5.4.0.
2. Use a single, code-based SDK initialization path. AndroidManifest removes
   StartAppInitProvider so it cannot initialize before LearnKoreanApp passes
   App ID 210777317. The manifest no longer duplicates the App ID metadata.
3. LearnKoreanApp uses the shared AdConfig.START_IO_APP_ID constant.
4. Bump versionCode from 1 to 2 and versionName from 1.0.0 to 1.0.1 for APKPure.

Important:
- Keep applicationId exactly com.shahabmillizai.learnkorean.
- Keep the existing release keystore and GitHub Actions secrets unchanged.
- Do not use Candy Blast App ID 210945361 in this project.
- Use .github/workflows/android-build.yml, which completed a signed release
  build successfully on 2026-11-06. The duplicate .github/workflows/build-apk.yml
  failed with "no main manifest attribute" while invoking its downloaded wrapper.
- After replacing files, push to main. Wait for Android CI/CD Build & Package to
  finish. Download learn-korean-release-apk, install it over the existing app
  (same signing key), and test the banner on Home.
- Interstitial is intentionally requested and shown only after three completed
  lessons. Rewarded video is requested only after tapping Unlock on the Bonus
  TOPIK pack.
- This fixes the integration path but cannot guarantee live ad fill. Start.io
  may return no-fill depending on demand, country, inventory, network, and review.
