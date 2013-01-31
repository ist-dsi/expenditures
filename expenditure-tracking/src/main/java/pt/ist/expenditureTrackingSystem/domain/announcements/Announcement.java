/*
 * @(#)Announcement.java
 *
 * Copyright 2009 Instituto Superior Tecnico
 * Founding Authors: Luis Cruz, Nuno Ochoa, Paulo Abrantes
 * 
 *      https://fenix-ashes.ist.utl.pt/
 * 
 *   This file is part of the Expenditure Tracking Module.
 *
 *   The Expenditure Tracking Module is free software: you can
 *   redistribute it and/or modify it under the terms of the GNU Lesser General
 *   Public License as published by the Free Software Foundation, either version 
 *   3 of the License, or (at your option) any later version.
 *
 *   The Expenditure Tracking Module is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public License
 *   along with the Expenditure Tracking Module. If not, see <http://www.gnu.org/licenses/>.
 * 
 */
package pt.ist.expenditureTrackingSystem.domain.announcements;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.Predicate;
import org.joda.time.DateTime;

import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.organization.Supplier;
import pt.ist.expenditureTrackingSystem.domain.organization.Unit;

/**
 * 
 * @author Luis Cruz
 * @author Paulo Abrantes
 * 
 */
public abstract class Announcement extends Announcement_Base {

	protected Announcement() {
		super();
		setExpenditureTrackingSystem(ExpenditureTrackingSystem.getInstance());
		setCreationDate(new DateTime());
	}

	public abstract Set<Unit> getBuyingUnits();

	public abstract Supplier getSupplier();

	public abstract Unit getRequestingUnit();

	public static <T extends Announcement> List<T> getAnnouncements(Class<T> clazz) {
		List<T> announcements = new ArrayList<T>();
		for (Announcement announcement : ExpenditureTrackingSystem.getInstance().getAnnouncements()) {
			if (clazz.isAssignableFrom(announcement.getClass())) {
				announcements.add((T) announcement);
			}
		}
		return announcements;
	}

	public static <T extends Announcement> List<T> getAnnouncements(Class<T> clazz, Predicate predicate) {
		List<T> announcements = new ArrayList<T>();
		for (Announcement announcement : ExpenditureTrackingSystem.getInstance().getAnnouncements()) {
			if (clazz.isAssignableFrom(announcement.getClass()) && predicate.evaluate(announcement)) {
				announcements.add((T) announcement);
			}
		}
		return announcements;
	}

	@Override
	public boolean isConnectedToCurrentHost() {
		return getExpenditureTrackingSystem() == ExpenditureTrackingSystem.getInstance();
	}

}
