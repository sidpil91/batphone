/**
 * Copyright (C) 2011 The Serval Project
 *
 * This file is part of Serval Software (http://www.servalproject.org)
 *
 * Serval Software is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * This source code is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this source code; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package org.servalproject.servald;

import org.servalproject.account.AccountService;

import android.content.Context;


public class Peer implements IPeer {
	public int score;
	public long contactId = -1;
	String contactName;
	public long cacheUntil = 0;
	public long lastSeen = 0;
	public boolean reachable = false;
	public final SubscriberId sid;

	// did / name resolved from looking up the sid
	public String did;
	public String name;

	// every peer must have a sid
	Peer(SubscriberId sid) {
		this.sid = sid;
	}

	@Override
	public String getSortString() {
		return getContactName() + did + sid;
	}

	public String getDisplayName() {
		if (name == null)
			return sid.abbreviation();
		return name;
	}

	@Override
	public boolean hasName() {
		return (contactName != null && !contactName.equals(""))
				|| (name != null && !name.equals(""));
	}

	public String getContactName() {
		if (contactName != null && !contactName.equals(""))
			return contactName;
		if (name != null && !name.equals(""))
			return name;
		return "";
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	@Override
	public boolean equals(Object o) {
		if (o == null)
			return false;
		if (!(o instanceof Peer))
			return false;
		Peer other = (Peer) o;
		return this.sid.equals(other.sid);
	}

	@Override
	public int hashCode() {
		return sid.hashCode();
	}

	@Override
	public String toString() {
		if (contactName != null && !contactName.equals(""))
			return contactName;
		if (name != null && !name.equals(""))
			return name;
		if (did != null && !did.equals(""))
			return did;
		return sid.abbreviation();
	}

	public boolean hasDid() {
		return did != null && !did.equals("");
	}

	public boolean stillAlive() {
		return reachable;
	}

	@Override
	public SubscriberId getSubscriberId() {
		return sid;
	}

	@Override
	public long getContactId() {
		return contactId;
	}

	@Override
	public void addContact(Context context) {
		if (contactId == -1) {
			contactId = AccountService.addContact(
					context, getContactName(), sid,
					did);
		}
	}
}
