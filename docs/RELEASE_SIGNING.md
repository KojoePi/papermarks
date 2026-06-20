# Release signing for Obtainium

Obtainium can install APK assets from GitHub Releases. Android updates require all future APKs to be signed with the same key.

## 1. Create a release keystore locally

```bash
bash scripts/create-release-keystore.sh
```

This creates `papermarks-release.jks` and prints a Base64 value.

## 2. Add GitHub repository secrets

In GitHub, open:

`Settings → Secrets and variables → Actions → New repository secret`

Add:

- `ANDROID_SIGNING_KEY`: Base64 content of `papermarks-release.jks`
- `ANDROID_KEY_ALIAS`: usually `papermarks`
- `ANDROID_KEYSTORE_PASSWORD`: your keystore password
- `ANDROID_KEY_PASSWORD`: your key password

Do not commit `.jks` files.

## 3. Create a release tag

```bash
git tag v0.1.0
git push origin v0.1.0
```

The workflow `.github/workflows/android-release.yml` builds and signs a release APK, then publishes it as a GitHub Release asset.

## 4. Install with Obtainium

In Obtainium:

1. Add App
2. Source URL: your GitHub repo URL
3. Source type: GitHub Releases
4. APK selection: choose the `app-release-signed.apk` asset
5. Install

## Important

If you lose the release keystore, users cannot update over the previous installation. Keep a secure backup of the keystore and passwords.
