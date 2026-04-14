package com.cems.cemsbackend.model;

/**
 * Constants representing the different user access levels within the system.
 */
public class AccessLevel {
  /** Standard user with basic permissions. */
  public static final int USER = 0;

  /** Faculty member with event creation permissions. */
  public static final int FACULTY = 1;

  /** System administrator with full management permissions. */
  public static final int ADMIN = 2;
}