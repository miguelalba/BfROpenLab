/*******************************************************************************
 * Copyright (c) 2015 Federal Institute for Risk Assessment (BfR), Germany
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
 *
 * Contributors:
 *     Department Biological Safety - BfR
 *******************************************************************************/
package de.bund.bfr.knime.openkrise;

import java.util.HashSet;
import java.util.Set;

public class MyDelivery {

	private int id;
	private int supplierID, recipientID;
	private Integer deliveryDay;
	private Integer deliveryMonth;
	private Integer deliveryYear;

	private Set<Integer> allNextIDs;
	private Set<Integer> allPreviousIDs;

	private MyHashSet<Integer> forwardDeliveries;
	private MyHashSet<Integer> backwardDeliveries;

	public MyDelivery(int id, int supplierID, int recipientID, Integer deliveryDay,
			Integer deliveryMonth, Integer deliveryYear) {
		this.id = id;
		this.supplierID = supplierID;
		this.recipientID = recipientID;
		this.deliveryDay = deliveryDay;
		this.deliveryMonth = deliveryMonth;
		this.deliveryYear = deliveryYear;

		allNextIDs = new HashSet<>();
		allPreviousIDs = new HashSet<>();
	}

	public Set<Integer> getAllNextIDs() {
		return allNextIDs;
	}

	public Set<Integer> getAllPreviousIDs() {
		return allPreviousIDs;
	}

	public MyHashSet<Integer> getForwardDeliveries() {
		return forwardDeliveries;
	}

	public void setForwardDeliveries(MyHashSet<Integer> forwardDeliveries) {
		this.forwardDeliveries = forwardDeliveries;
	}

	public MyHashSet<Integer> getBackwardDeliveries() {
		return backwardDeliveries;
	}

	public void setBackwardDeliveries(MyHashSet<Integer> backwardDeliveries) {
		this.backwardDeliveries = backwardDeliveries;
	}

	public void setSupplierID(int supplierID) {
		this.supplierID = supplierID;
	}

	public void setRecipientID(int recipientID) {
		this.recipientID = recipientID;
	}

	public int getSupplierID() {
		return supplierID;
	}

	public int getRecipientID() {
		return recipientID;
	}

	public int getId() {
		return id;
	}

	public Integer getDeliveryDay() {
		return deliveryDay;
	}

	public Integer getDeliveryMonth() {
		return deliveryMonth;
	}

	public Integer getDeliveryYear() {
		return deliveryYear;
	}

	public void setDeliveryDay(Integer deliveryDay) {
		this.deliveryDay = deliveryDay;
	}

	public void setDeliveryMonth(Integer deliveryMonth) {
		this.deliveryMonth = deliveryMonth;
	}

	public void setDeliveryYear(Integer deliveryYear) {
		this.deliveryYear = deliveryYear;
	}

	public void resetStatusVariables() {
		forwardDeliveries = null;
		backwardDeliveries = null;
	}

	@Override
	public MyDelivery clone() {
		MyDelivery mdNew = new MyDelivery(getId(), getSupplierID(), getRecipientID(),
				getDeliveryDay(), getDeliveryMonth(), getDeliveryYear());

		mdNew.getAllNextIDs().addAll(getAllNextIDs());
		mdNew.getAllPreviousIDs().addAll(getAllPreviousIDs());

		return mdNew;
	}
}
