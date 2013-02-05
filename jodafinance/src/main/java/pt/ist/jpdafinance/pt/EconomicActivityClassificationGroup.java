/* 
* @(#)EconomicActivityClassificationGroup.java 
* 
* Copyright 2012 Instituto Superior Tecnico 
* Founding Authors: Luis Cruz, Susana Fernandes 
*  
*      https://fenix-ashes.ist.utl.pt/ 
*  
*   This file is part of the JodaFinance library. 
* 
*   The JodaFinance library is free software: you can 
*   redistribute it and/or modify it under the terms of the GNU Lesser General 
*   Public License as published by the Free Software Foundation, either version  
*   3 of the License, or (at your option) any later version. 
* 
*   JodaFinance is distributed in the hope that it will be useful, 
*   but WITHOUT ANY WARRANTY; without even the implied warranty of 
*   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
*   GNU Lesser General Public License for more details. 
* 
*   You should have received a copy of the GNU Lesser General Public License 
*   along with JodaFinance. If not, see <http://www.gnu.org/licenses/>. 
*  
*/
package pt.ist.jpdafinance.pt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;

import pt.ist.jpdafinance.util.FileUtil;

/**
 * 
 * @author Luis Cruz
 * @author Susana Fernandes
 * 
 */
public class EconomicActivityClassificationGroup extends EconomicActivityClassification {

    private static final String RESOURCE_PATH = "/pt/ist/jpdafinance/pt/";

    static SortedSet<EconomicActivityClassificationGroup> classificationGroups =
            new TreeSet<EconomicActivityClassificationGroup>();

    static {
        try {
            final String[] content = FileUtil.readResource(RESOURCE_PATH + "EconomicActivityClassifications.csv");

            for (int i = 0; i < content.length; i++) {
                final String line = content[i];
                final String[] parts = line.split("\t");
                final String groupCode = parts[0].trim();
                final String description = parts[1].trim();
                final EconomicActivityClassificationGroup group = new EconomicActivityClassificationGroup(groupCode, description);

                final Collection<EconomicActivityClassification> EconomicActivityClassifications =
                        constructClassifications(content, i, group);
                i += EconomicActivityClassifications.size();

                classificationGroups.add(group);
            }
        } catch (Throwable t) {
            t.printStackTrace();
            System.out.println("###################################################################################");
            throw new Error(t);
        }
    }

    final SortedSet<EconomicActivityClassification> classifications = new TreeSet<EconomicActivityClassification>();

    public EconomicActivityClassificationGroup(final String code, final String description) {
        super(code, description);
    }

    private static Collection<EconomicActivityClassification> constructClassifications(final String[] content, final int i,
            final EconomicActivityClassificationGroup group) {
        final Collection<EconomicActivityClassification> set = new ArrayList<EconomicActivityClassification>();
        for (int j = i + 1; j < content.length; j++) {
            final String line = content[j];
            final String[] parts = line.split("\t");
            final String groupCode = parts[0].trim();
            if (!groupCode.isEmpty()) {
                return set;
            }
            final String code = parts[1];
            final String description = parts[2];
            set.add(new EconomicActivityClassificationLeaf(code, description, group));
        }
        return set;
    }

    public static SortedSet<EconomicActivityClassificationGroup> getCassificationGroups() {
        return Collections.unmodifiableSortedSet(classificationGroups);
    }

    public SortedSet<EconomicActivityClassification> getClassifications() {
        return Collections.unmodifiableSortedSet(classifications);
    }

    public static EconomicActivityClassificationLeaf importFromString(final String string) {
        if (string != null && !string.isEmpty()) {
            final String[] parts = string.split(":");
            for (final EconomicActivityClassificationGroup group : classificationGroups) {
                if (group.code.equals(parts[0])) {
                    for (final EconomicActivityClassification classification : group.classifications) {
                        if (classification.code.equals(parts[1])) {
                            return (EconomicActivityClassificationLeaf) classification;
                        }
                    }
                }
            }
        }
        return null;
    }

}
