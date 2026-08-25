plugins { id("com.android.application"); kotlin("android") }

android { namespace="com.swapnil.studio.security"; compileSdk=35
 defaultConfig { applicationId="com.swapnil.studio.security"; minSdk=26; targetSdk=28; versionCode=141; versionName="1.4.1" }
 compileOptions { sourceCompatibility=JavaVersion.VERSION_17; targetCompatibility=JavaVersion.VERSION_17 }
 kotlinOptions { jvmTarget="17" }
}
