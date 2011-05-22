/*
 *  Copyright (C) 2010 Rick Charon
 * 
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 * 
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package ptscharts;

import java.util.Date;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * OrderTableDateRenderer.java Created Aug 30, 2010 2:33:56 PM in Project PeTraSys
 *  
 * @author Rick Charon
 * 
 */
public  class PtsOrderTableDateRenderer extends DefaultTableCellRenderer {
    //DateFormat formatter;

    public PtsOrderTableDateRenderer() {
      super();
    }

    @Override
    public void setValue(Object value) {
//        if (formatter==null) {
//            formatter = DateFormat.getDateInstance();
//        }
//        setText((value == null) ? "" : formatter.format(value));
      setText((value == null) ? "" : PtsDateOps.prettyString((Date) value));

    }
  }

