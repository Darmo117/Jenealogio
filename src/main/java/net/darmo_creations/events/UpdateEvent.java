/*
 * Copyright © 2017 Damien Vergnet
 * 
 * This file is part of Jenealogio.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.darmo_creations.events;

import net.darmo_creations.util.Version;

/**
 * Base class for all update related events.
 *
 * @author Damien Vergnet
 */
public abstract class UpdateEvent extends AbstractEvent {
  @Override
  public boolean isCancelable() {
    return false;
  }

  /**
   * This event is fired just before checking for updates. If it is canceled, updates checking will
   * be canceled.
   *
   * @author Damien Vergnet
   */
  public static class Checking extends UpdateEvent {
    @Override
    public boolean isCancelable() {
      return true;
    }
  }

  /**
   * This event is fired when a new update is available.
   *
   * @author Damien Vergnet
   */
  public static class NewUpdate extends UpdateEvent {
    private final Version version;
    private final String link, changelog;

    /**
     * Creates an event.
     * 
     * @param version the new version
     * @param link the download link
     * @param changelog the html changelog
     */
    public NewUpdate(Version version, String link, String changelog) {
      this.version = version;
      this.link = link;
      this.changelog = changelog;
    }

    public Version getVersion() {
      return this.version;
    }

    public String getLink() {
      return this.link;
    }

    public String getChangelog() {
      return this.changelog;
    }
  }

  /**
   * This event is fired when no updates are available.
   *
   * @author Damien Vergnet
   */
  public static class NoUpdate extends UpdateEvent {}

  /**
   * This event is fired when update checking failed.
   *
   * @author Damien Vergnet
   */
  public static class CheckFailed extends UpdateEvent {
    private final String reason;

    /**
     * Creates an event.
     * 
     * @param reason the reason why update checking failed
     */
    public CheckFailed(String reason) {
      this.reason = reason;
    }

    public String getReason() {
      return this.reason;
    }
  }
}
