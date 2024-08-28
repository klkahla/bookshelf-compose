package com.example.bookshelf.utils

import android.content.pm.PackageManager

object Utils {
    // Helper function to check if a package is installed
    fun isPackageInstalled(packageName: String, packageManager: PackageManager): Boolean {
        return try {
            packageManager.getPackageInfo(packageName, 0)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }
}