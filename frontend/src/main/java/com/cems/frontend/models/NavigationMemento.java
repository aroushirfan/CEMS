package com.cems.frontend.models;

/**
 * A memento class to store the state of navigation, including the path
 * and any associated payload.
 */
public class NavigationMemento {
  private Paths path;
  private Object payload;

  /**
   * Constructs a NavigationMemento with the specified path and payload.
   *
   * @param path    the navigation path
   * @param payload the associated payload (can be null)
   */
  public NavigationMemento(Paths path, Object payload) {
    this.path = path;
    this.payload = payload;
  }

  /**
   * Gets the navigation path.
   *
   * @return the navigation path
   */
  public Paths getPath() {
    return path;
  }

  /**
   * Gets the payload associated with this navigation state.
   *
   * @return the payload, or null if there is no payload
   */
  public <T> T getPayload(Class<T> type) {
    return type.isInstance(payload) ? type.cast(payload) : null;
  }
}
