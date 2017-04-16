package net.darmo_creations.util;

/**
 * A class can implement the <code>Observer</code> interface when it wants to be informed of changes
 * in observable objects.
 *
 * @author Damien Vergnet
 */
public interface Observer {
  /**
   * This method is called whenever the observed object is changed. An application calls an
   * <tt>Observable</tt> object's <code>notifyObservers</code> method to have all the object's
   * observers notified of the change.
   *
   * @param obs the observable object.
   * @param o an argument passed to the <code>notifyObservers</code> method.
   */
  void update(Observable obs, Object o);
}
