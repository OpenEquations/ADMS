package rw.adms.domain.users.enums;

/**
 * A grantable slice of the system a non-superadmin user can be given access
 * to. Holding a permission means full (view + manage) access to that module;
 * not holding it means the module is hidden from that user entirely.
 * Superadmins implicitly hold every permission - see {@link rw.adms.domain.users.User#hasPermission}.
 */
public enum Permission {

    MANAGE_ITEMS,

    MANAGE_WAREHOUSES,

    MANAGE_COMPANIES,

    MANAGE_TENDERS
}
