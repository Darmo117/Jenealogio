package net.darmo_creations.util;

/**
 * This interface represents an observable object. It can be implemented to represent an object that
 * the application wants to have observed.
 * <p>
 * An observable object can have one or more observers. An observer may be any object that
 * implements interface <tt>Observer</tt>. After an observable instance changes, an application
 * calling the <code>Observable</code>'s <code>notifyObservers</code> method causes all of its
 * observers to be notified of the change by a call to their <code>update</code> method.
 * </p>
 * <p>
 * The order in which notifications will be delivered is unspecified. When an observable object is
 * newly created, its set of observers is empty.
 * </p>
 * 
 * @author Damien Vergnet
 */
public interface Observable {
  /**
   * Adds an observer to the set of observers for this object, provided that it is not the same as
   * some observer already in the set. The order in which notifications will be delivered to
   * multiple observers is not specified. See the class comment.
   *
   * @param observer an observer to be added.
   * @throws NullPointerException if the parameter o is null.
   */
  void addObserver(Observer observer);

  /**
   * Deletes an observer from the set of observers of this object. Passing <code>null</code> to this
   * method will have no effect.
   * 
   * @param observer the observer to be deleted.
   */
  void removeObserver(Observer observer);

  /**
   * Notifies all of its observers.
   * <p>
   * Each observer has its <code>update</code> method called with two arguments: this observable
   * object and the <code>arg</code> argument.
   * </p>
   *
   * @param o any object.
   */
  void notifyObservers(Object o);
}
