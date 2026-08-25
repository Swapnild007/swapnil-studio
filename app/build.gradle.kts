plugins { id("com.android.application"); kotlin("android") }

android { namespace="com.swapnil.studio.security"; compileSdk=35
 defaultConfig { applicationId="com.swapnil.studio.security"; minSdk=26; targetSdk=35; versionCode=140; versionName="1.4.0" }
 compileOptions { sourceCompatibility=JavaVersion.VERSION_17; targetCompatibility=JavaVersion.VERSION_17 }
 kotlinOptions { jvmTarget="17" }
}
