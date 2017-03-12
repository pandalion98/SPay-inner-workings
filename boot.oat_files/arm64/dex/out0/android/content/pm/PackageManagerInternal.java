package android.content.pm;

public abstract class PackageManagerInternal {

    public interface PackagesProvider {
        String[] getPackages(int i);
    }

    public interface SyncAdapterPackagesProvider {
        String[] getPackages(String str, int i);
    }

    public abstract void grantDefaultPermissionsToDefaultDialerApp(String str, int i);

    public abstract void grantDefaultPermissionsToDefaultSimCallManager(String str, int i);

    public abstract void grantDefaultPermissionsToDefaultSmsApp(String str, int i);

    public abstract void setDialerAppPackagesProvider(PackagesProvider packagesProvider);

    public abstract void setImePackagesProvider(PackagesProvider packagesProvider);

    public abstract void setLocationPackagesProvider(PackagesProvider packagesProvider);

    public abstract void setSimCallManagerPackagesProvider(PackagesProvider packagesProvider);

    public abstract void setSmsAppPackagesProvider(PackagesProvider packagesProvider);

    public abstract void setSyncAdapterPackagesprovider(SyncAdapterPackagesProvider syncAdapterPackagesProvider);

    public abstract void setVoiceInteractionPackagesProvider(PackagesProvider packagesProvider);
}
