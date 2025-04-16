package org.sportstogo.backend.Enums;

/**
 * Enum representing the role of a user within a group.
 * It defines three possible roles:
 * <ul>
 *     <li><b>admin:</b> The administrator of the group with full privileges.</li>
 *     <li><b>co_admin:</b> A co-administrator with some administrative privileges, but not full control.</li>
 *     <li><b>member:</b> A standard user within the group with limited privileges.</li>
 * </ul>
 */
public enum Role {
    /**
     * Admin role, representing the user with the highest level of control within the group.
     */
    admin,

    /**
     * Co-admin role, representing a user with some administrative privileges, but not the full level of control like the admin.
     */
    co_admin,

    /**
     * Member role, representing a user with the least privileges in the group.
     */
    member
}
