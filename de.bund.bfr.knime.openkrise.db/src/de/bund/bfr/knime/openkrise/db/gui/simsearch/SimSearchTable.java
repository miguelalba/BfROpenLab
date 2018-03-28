/*******************************************************************************
 * Copyright (c) 2017 German Federal Institute for Risk Assessment (BfR)
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
package de.bund.bfr.knime.openkrise.db.gui.simsearch;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.ItemSelectable;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.ScrollPane;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentListener;
import java.awt.event.ContainerListener;
import java.awt.event.FocusListener;
import java.awt.event.HierarchyBoundsListener;
import java.awt.event.HierarchyListener;
import java.awt.event.InputMethodListener;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.VetoableChangeListener;
import java.io.IOException;
import java.security.spec.ECGenParameterSpec;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.DefaultListSelectionModel;
import javax.swing.DropMode;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JViewport;
import javax.swing.KeyStroke;
import javax.swing.MenuSelectionManager;
import javax.swing.RowSorter.SortKey;
import javax.swing.ScrollPaneConstants;
import javax.swing.SortOrder;
import javax.swing.SwingUtilities;
import javax.swing.TransferHandler;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import javax.swing.event.AncestorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.MenuDragMouseListener;
import javax.swing.event.MenuKeyListener;
import javax.swing.event.RowSorterEvent;
import javax.swing.event.RowSorterListener;
import javax.swing.event.TableModelEvent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.MenuItemUI;
import javax.swing.plaf.TableUI;
import javax.swing.plaf.basic.BasicCheckBoxMenuItemUI;
import javax.swing.plaf.basic.BasicMenuItemUI;
import javax.swing.plaf.basic.BasicTableUI;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import org.eclipse.osgi.internal.debug.Debug;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.sun.javafx.scene.control.ReadOnlyUnbackedObservableList;

import de.bund.bfr.knime.openkrise.db.gui.dbtable.header.GuiMessages;
import de.bund.bfr.knime.openkrise.db.gui.simsearch.SimSearchJTable.RowHeaderColumnRenderer;
import javafx.scene.input.MouseButton;
import sun.swing.SwingUtilities2;

public class SimSearchTable extends JScrollPane{

  public static class ViewSettings {
    
    public static final int ROW_HEIGHT_MIN = 20;
    public static final int ROW_HEIGHT_MAX = 100;
    public static final int FONT_SIZE_MIN=6;
    public static final int FONT_SIZE_MAX=12;
    private static final int CELL_MARGIN_Y_DEFAULT = 2;
    private static final int CELL_MARGIN_X_DEFAULT = 5;
  
    
    private boolean showRemovedObjects;
    private Map<SimSearch.SimSet.Type, List<String>> columnOrder;
    private Map<SimSearch.SimSet.Type, List<String>> frozenColumns;
    private Map<SimSearch.SimSet.Type, Set<String>> invisibleColumns;
    private Map<SimSearch.SimSet, Map<Integer,Integer>> columnWidths;
    private Map<SimSearch.SimSet.Type, Map<Integer, Integer>> rowHeights;
    private Map<SimSearch.SimSet, List<Integer>> rowOrder;
    private Map<SimSearch.SimSet, List<SortKey>> sortKeys;
    private boolean useSimpleRowHeightAsDefault;
    private Font font;
    protected int cellMarginY;
    protected int cellMarginX;
    
    public ViewSettings() {
      this.columnOrder = new HashMap<>();
      this.frozenColumns = new HashMap<>();
      this.invisibleColumns = this.createDefaultInvisibleColumnsMap();
      this.columnWidths = new HashMap<>();
      this.rowHeights = new HashMap<>();
      this.rowOrder = new HashMap<>();
      this.sortKeys = new HashMap<>();
      this.useSimpleRowHeightAsDefault = true;
      this.cellMarginX = CELL_MARGIN_X_DEFAULT;
      this.cellMarginY = CELL_MARGIN_Y_DEFAULT;
      this.font = UIManager.getFont("Table.font");
    }
    
    private Map<SimSearch.SimSet.Type, Set<String>> createDefaultInvisibleColumnsMap() {
    	Map<SimSearch.SimSet.Type, Set<String>> result = new HashMap();
    	result.put(SimSearch.SimSet.Type.STATION, new HashSet<>(Arrays.asList("ID","Produktkatalog", "DatumEnde", "DatumHoehepunkt", "Erregernachweis", "Latitude", "EMail", "Betriebsnummer", "Longitude", "Code", "AlterMax", "ImportSources", "Webseite", "Ansprechpartner", "AnzahlFaelle", "DatumBeginn", "Serial", "CasePriority", "Fax", "Telefon", "VATnumber", "AlterMin")));
    	result.put(SimSearch.SimSet.Type.PRODUCT, new HashSet<>(Arrays.asList("ID", "IntendedUse", "Serial", "Matrices", "Prozessierung", "Code", "ImportSources")));
    	result.put(SimSearch.SimSet.Type.LOT, new HashSet<>(Arrays.asList("ID", "Serial", "pd_month", "OriginCountry", "pd_year", "MicrobioSample", "pd_day", "ImportSources")));
    	result.put(SimSearch.SimSet.Type.DELIVERY, new HashSet<>(Arrays.asList("ID", "Explanation_EndChain", "Serial", "EndChain", "Unitmenge", "Contact_Questions_Remarks", "Further_Traceback", "ImportSources", "UnitEinheit")));
    	return result;
    }
    
    protected Font getFont() { return this.font; }
  }
  
  private TableColumnModel tableColumnModel;
  private TableColumnModel rowHeaderColumnModel;
  private List<Integer> frozenColumns = new ArrayList<>(Arrays.asList(2));
  private Set<Integer> invisibleColumns = new HashSet<>(Arrays.asList(1));
  private SimSearchJTable table;
  private SimSearchJTable rowHeaderColumnTable;
  private ViewSettings viewSettings;
  private JTextField filterTextBox;
  private JCheckBox useRegexFilterCheckBox;
  private Color filterColor;
  //private Set<Integer> selectedModelIndices;
  private AbstractButton inactiveRowFilterSwitch;
  private JButton ignoreSimSetButton;
  private JButton ignoreAllPairsInSimSetButton;
  private int[] rowHeights;
  //private ActionListener simSetIgnoreSwitchActionListener;
 
  
  
  private JPopupMenu popupMenu;
  private CopyAdapter copyAdapter;


  public SimSearchTable(ViewSettings viewSettings) {
    super(); 
    this.viewSettings = viewSettings;
    this.init();
  }

  private void init() {
    this.viewSettings = new ViewSettings();
    this.initTables();
    //this.selectedModelIndices = new HashSet<>();
    
    // Put it in a viewport that we can control a bit
    JViewport jv = new JViewport();
    jv.setView(this.rowHeaderColumnTable);
    jv.setPreferredSize(this.rowHeaderColumnTable.getMaximumSize());

    // With out shutting off autoResizeMode, our tables won't scroll
    // correctly (horizontally, anyway)
    table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

    this.setRowHeader(jv);
    
    
    this.setCorner(ScrollPaneConstants.UPPER_LEFT_CORNER, this.rowHeaderColumnTable
        .getTableHeader());
  }

  @SuppressWarnings("serial")	private void initColumnModels() {
    this.tableColumnModel = new DefaultTableColumnModel() {
      private static final int COLUMN_WIDTH_MIN = 10;
      private static final int COLUMN_WIDTH_MAX = Integer.MAX_VALUE;
      
      @Override 
      public void addColumn(TableColumn tc) {
        //StackTraceElement[] cause = Thread.currentThread().getStackTrace();
        // Ignore row header column, frozen columns and invisible columns
        if(tc.getModelIndex()==0 || frozenColumns.contains(tc.getModelIndex()) || invisibleColumns.contains(tc.getModelIndex())) {  
          return;
        }
        tc.setMinWidth(COLUMN_WIDTH_MIN); // just for looks, really...
        tc.setMaxWidth(COLUMN_WIDTH_MAX);
        tc.setResizable(true);
        super.addColumn(tc);
      }
    };

    this.rowHeaderColumnModel = new DefaultTableColumnModel() {
      private static final int ROW_HEADER_WIDTH = 40;
      private static final int FREEZE_COLUMN_MAX_WIDTH = 200;
      private static final int FREEZE_COLUMN_MIN_WIDTH = 20;
      
      public void addColumn(TableColumn tc) {
        if(tc.getModelIndex()==0 || frozenColumns.contains(tc.getModelIndex())) {
          if(tc.getModelIndex()==0) {
            // RowHeader column
            tc.setMaxWidth(ROW_HEADER_WIDTH); //    tc.getPreferredWidth());
            tc.setMinWidth(ROW_HEADER_WIDTH);
            tc.setResizable(false);
          } else {
            tc.setMinWidth(FREEZE_COLUMN_MIN_WIDTH);
            tc.setMaxWidth(FREEZE_COLUMN_MAX_WIDTH);
          }
          //tc.setMaxWidth(tc.getPreferredWidth());
          super.addColumn(tc);
        }
        // Drop the rest of the columns . . . this is the header column
        // only
      }
    };
  }

  private void addMouseListenerToTable() {
    MouseListener mouseListener = new MouseAdapter() {

      @Override
      public void mouseClicked(MouseEvent e) {
        if(e.getButton()==MouseEvent.BUTTON1) {
          e.consume();
          SimSearchTable.this.table.updateUI();
          SimSearchTable.this.rowHeaderColumnTable.updateUI();
        }
      }

    };

    this.table.getTableHeader().addMouseListener(mouseListener);
  }
  
  private void addDragAndDropFeature() {
    SimSearchTableRowTransferHandler transferHandler = new SimSearchTableRowTransferHandler(this);
//    MouseAdapter mouseAdapter = new MouseAdapter() {
//
//		@Override
//		public void mouseDragged(MouseEvent e) {
//			SimSearchTable.this.repaintDropLocation(e);
//			SimSearchTable.this.updateUI();
//		}
//
//		@Override
//		public void mouseReleased(MouseEvent e) {
//			SimSearchTable.this.repaintDropLocation(e);
//			SimSearchTable.this.updateUI();
//			
//		}
//    	
//    };
    Repainter dropLocationRepainter = new Repainter();
    
    Arrays.asList(this.table,this.rowHeaderColumnTable).forEach( t -> {
      t.setTransferHandler(transferHandler);
      t.setDropMode(DropMode.ON_OR_INSERT_ROWS);
      t.setDragEnabled(true);
      t.addPropertyChangeListener("dropLocation", dropLocationRepainter);
      //t.addMouseMotionListener(mouseAdapter);
      //t.addMouseListener(mouseAdapter);
    });
  }
  
  
  private void addRowResizeFeature() {
	  this.rowHeaderColumnTable.setRowResizingAllowed(true);
  }
  
  private void freezeUnfreezeColumn(int columnModelIndex) {
    if(this.frozenColumns.contains(columnModelIndex)) {
      // unfreeze operation
      int columnViewIndex = this.rowHeaderColumnTable.convertColumnIndexToView(columnModelIndex);
      TableColumn tableColumn = this.rowHeaderColumnTable.getColumnModel().getColumn(columnViewIndex);
      this.rowHeaderColumnModel.removeColumn(tableColumn);
      //this.rowHeaderColumnTable.removeColumn(tableColumn);
      this.frozenColumns.remove((Integer) columnModelIndex);
      //this.table.addColumn(tableColumn);
      tableColumn.setResizable(true);
      this.tableColumnModel.addColumn(tableColumn);
      this.tableColumnModel.moveColumn(this.tableColumnModel.getColumnCount()-1, 0);
    } else if(this.frozenColumns.isEmpty()) {
      // this is a valid freeze operation
      int columnViewIndex = this.table.convertColumnIndexToView(columnModelIndex);
      TableColumn tableColumn = this.table.getColumnModel().getColumn(columnViewIndex); 
      this.table.removeColumn(tableColumn);
      //this.tableColumnModel.removeColumn(tableColumn);
      this.frozenColumns.add(columnModelIndex);
      //this.rowHeaderColumnModel.addColumn(tableColumn);
      this.rowHeaderColumnTable.addColumn(tableColumn);
      //this.updateView();
      
    } else {
      return;
    }
    this.updateTablePosition();
  }
  
  
  
  private void updateTablePosition() {
    JViewport jv = this.getRowHeader();
    if(jv==null) return;
    
    jv.setPreferredSize(this.rowHeaderColumnTable.getMaximumSize());
  }
  
  private void showHeaderPopUp(Component comp, int x, int y) {
    this.popupMenu.removeAll();
    
    JTableHeader tableHeader = (JTableHeader) comp;
    int columnIndex = tableHeader.columnAtPoint(new Point(x,y));
    int columnModelIndex = tableHeader.getTable().convertColumnIndexToModel(columnIndex);
    
    if(columnModelIndex>0) {
      JMenuItem menuItem = new JMenuItem();
      if(this.frozenColumns.contains(columnModelIndex))
        menuItem.setText("unfreeze");
      else {
        menuItem.setText("freeze");
        menuItem.setEnabled(this.frozenColumns.isEmpty());
      }
      menuItem.addActionListener(new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent arg0) {
          SimSearchTable.this.freezeUnfreezeColumn(columnModelIndex);          
        }
        
      });
      this.popupMenu.add(menuItem);
    }
    
    JMenu showColumnsMenu = new JMenu("Show columns");
    this.addShowColumnsSubMenuItems(showColumnsMenu);
    this.popupMenu.add(showColumnsMenu);
    //this.popupMenu.show(comp, x, y);
    this.popupMenu.show(tableHeader.getTable(), x, y);
  }
  
  private void showRowPopUp(Component comp, int x, int y) {
	    this.popupMenu.removeAll();
	    
	    JTable table = (JTable) comp;
	    Point p = new Point(x,y);
	    int columnIndex = table.columnAtPoint(p);
	    int rowAtPoint = table.rowAtPoint(p);
	    if(rowAtPoint<0 || columnIndex<0) return;
	    
	    int[] selectedRows = table.getSelectedRows();
	    int[] rowIndices;
	    if(IntStream.of(selectedRows).anyMatch(i -> i==rowAtPoint)) {
	    	// take all selected Rows
	    	rowIndices = selectedRows;
	    } else {
	    	rowIndices = new int[] {rowAtPoint};
	    }
	    
	    int columnModelIndex = table.convertColumnIndexToModel(columnIndex);
	    int[] rowModelIndices = new int[rowIndices.length];
	    for(int i=0; i<rowIndices.length; ++i) rowModelIndices[i] = table.convertRowIndexToModel(rowIndices[i]);
	    
	    boolean isUnmergeAvailable = IntStream.of(rowModelIndices).allMatch(i -> (this.getModel().isMerged(i) || this.getModel().getMergeCount(i)>0));
	    
	    
      JMenuItem unmergeMenuItem = new JMenuItem("unmerge");
      unmergeMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_U, KeyEvent.CTRL_MASK));
      unmergeMenuItem.getAccessibleContext().setAccessibleDescription(
          "Unmerge rows.");
      unmergeMenuItem.setEnabled(isUnmergeAvailable);
      
      unmergeMenuItem.addActionListener(new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent arg0) {
          SimSearchTable.this.getModel().unmergeRows(rowModelIndices);          
        }
        
      });
      this.popupMenu.add(unmergeMenuItem);
      
      if(this.copyAdapter!=null) {
	      JMenuItem copyMenuItem = new JMenuItem("copy");
	      copyMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.CTRL_MASK));
	      copyMenuItem.getAccessibleContext().setAccessibleDescription(
	          "Copy data to clipboard.");
	      //unmergeMenuItem.setEnabled(isUnmergeAvailable);
	      
	      copyMenuItem.addActionListener(new ActionListener() {
	
	        @Override
	        public void actionPerformed(ActionEvent arg0) {
	          copyAdapter.copyData(table, rowIndices, columnIndex);          
	        }
	        
	      });
	      this.popupMenu.add(copyMenuItem);
      }
	    
	    this.popupMenu.show(table, x, y);
  }
  
  private void addShowColumnsSubMenuItems(JMenu menu) {
    //JPanel panel = new JPanel(); 
    for(int column=1; column<this.getModel().getColumnCount(); ++column) {
      
      JCheckBoxMenuItem item = new ColumnVisibilityCheckBoxMenuItem(GuiMessages.getString(this.getModel().getColumnName(column)),column);
      item.setSelected(!this.invisibleColumns.contains(column));
      //item.addActionListener(new SwitchColumnVisibilityActionListener(column));
      //panel.add(item);
      menu.add(item);
    }
    //menu.add(panel);
  }
  
  private void setColumnVisible(int modelColumnIndex, boolean visible) {
    if(visible) {
      if(!this.invisibleColumns.contains(modelColumnIndex)) return;
      this.invisibleColumns.remove((Integer) modelColumnIndex);
      TableColumn tableColumn = new TableColumn(modelColumnIndex);
      this.table.addColumn(tableColumn);
      this.table.initColumnRenderer(tableColumn);
      this.table.adjustColumnWidth(this.table.getColumnCount()-1);
    } else {
      if(this.invisibleColumns.contains(modelColumnIndex)) return;
      this.invisibleColumns.add(modelColumnIndex);
      if(this.frozenColumns.contains((Integer) modelColumnIndex)) {
        // this is a frozen column
        this.frozenColumns.remove((Integer) modelColumnIndex);
        int viewColumnIndex = this.rowHeaderColumnTable.convertColumnIndexToView(modelColumnIndex);
        if(viewColumnIndex>=0) {
          TableColumn tableColumn = this.rowHeaderColumnTable.getColumnModel().getColumn(viewColumnIndex); 
          this.rowHeaderColumnTable.getColumnModel().removeColumn(tableColumn);
          this.updateTablePosition();
        }
      } else {
        // not a frozen column
        int viewColumnIndex = this.table.convertColumnIndexToView(modelColumnIndex);
        if(viewColumnIndex>=0) {
          TableColumn tableColumn = this.table.getColumnModel().getColumn(viewColumnIndex); 
          this.table.getColumnModel().removeColumn(tableColumn);
        }
      }
    }
    this.updateVisibleSorterColumns(this.getRowSorter());
  }
  
  private void updateVisibleSorterColumns(SimSearchRowSorter rowSorter) {
	  //SimSearchRowSorter rowSorter = this.getRowSorter();
	  if(rowSorter!=null) {
	    List<Integer> visibleColumns = new ArrayList<>();
        for(JTable t: Arrays.asList(this.table, this.rowHeaderColumnTable)) for(int i=(t==this.rowHeaderColumnTable?1:0); i<t.getColumnCount(); ++i) visibleColumns.add(t.convertColumnIndexToModel(i));
        rowSorter.setVisibleColumns(Collections.unmodifiableList(visibleColumns));
	  }
  }
  
  private void initTables() {
    this.initColumnModels();

    this.table = new SimSearchJTable(this.viewSettings);
    this.table.setAutoCreateColumnsFromModel(false);
    
    this.getViewport().setView(this.table);
    this.rowHeaderColumnTable = new SimSearchJTable(this.viewSettings);
    this.rowHeaderColumnTable.setAutoCreateColumnsFromModel(false);

    this.table.setColumnModel(this.tableColumnModel);
    this.rowHeaderColumnTable.setColumnModel(this.rowHeaderColumnModel);
    this.rowHeaderColumnTable.getTableHeader().setReorderingAllowed(false);
    
    this.table.setPartnerTable(this.rowHeaderColumnTable);
    this.rowHeaderColumnTable.setPartnerTable(this.table);
    
    this.addPopupFeature();
    this.addFrozenColumnsResizeFeature();
    //this.addSelectionAndRowHeightUpdateOnSortFix();
    
    // Make sure that selections between the main table and the header stay
    // in sync (by sharing the same model)
    table.setSelectionModel(this.rowHeaderColumnTable.getSelectionModel());
    //this.rowHeaderColumnTable.setUpdateSelectionOnSort(false);
    
//    // the selection is not automatically updated with row sequence change
//    table.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
//    	
//      public void valueChanged(ListSelectionEvent event) {
//        SimSearchTable.this.proccessSelectionEvent(event);
//      }
//    });

    
    this.addMouseListenerToTable();
    this.addDragAndDropFeature();
    this.addRowResizeFeature();
    this.addCopyFeature();
    
    //this.rowHeaderColumnTable.setUpdateSelectionOnSort(false);
  }
  
  private void addCopyFeature() {
	  this.copyAdapter = new CopyAdapter(this.table, this.rowHeaderColumnTable);
  }
  
  private void addSelectionAndRowHeightUpdateOnSortFix(SimSearchRowSorter rowSorter) {
	  rowSorter.addRowSorterListener(new RowSorterListener() {

		@Override
		public void sorterChanged(RowSorterEvent e) {
			
			// update row selection
			int[] selectedRows = table.getSelectedRows();
			//System.out.println("Rows in view (old): " + e.getPreviousRowCount() );
			//System.out.println("Rows in view (cur): " + table.getRowCount() );
//			System.out.println("\nNew sort event: ");
//			System.out.println("SelectedoViewIndices: \t" + Arrays.toString(selectedRows));
//			//System.out.println("SelectedModelIndexes: ");
//			int[] selectedModelIndices = new int[selectedRows.length];
//			for(int i=0; i<selectedRows.length; ++i) selectedModelIndices[i] = e.convertPreviousRowIndexToModel(selectedRows[i]);
//			int[] oldModelSeq = new int[e.getPreviousRowCount()];
//			for(int i=0; i<e.getPreviousRowCount(); ++i) oldModelSeq[i] = e.convertPreviousRowIndexToModel(i);
//			System.out.println("OldModelSequence:     \t" + Arrays.toString(oldModelSeq));
//			
//			System.out.println("SelectedModelIndices: \t" + Arrays.toString(selectedModelIndices));
//			List<Integer> newSelectedViewIndices = new ArrayList<>();
//			int[] newModelSeq = new int[e.getSource().getViewRowCount()];
//			for(int i=0; i<newModelSeq.length; ++i) newModelSeq[i] = e.getSource().convertRowIndexToModel(i);
//			System.out.println("NewModelSequence:     \t" + Arrays.toString(newModelSeq));
//			
			table.clearSelection();
			for(int i=0; i<selectedRows.length; ++i) { 
				int modelIndex = e.convertPreviousRowIndexToModel(selectedRows[i]);
				int viewIndex = ( modelIndex>=0 ? e.getSource().convertRowIndexToView(modelIndex) : -1 );
				if(viewIndex>=0) {
					//newSelectedViewIndices.add(viewIndex);
					table.addRowSelectionInterval(viewIndex, viewIndex);
				}
			}
//			System.out.println("SelectednViewIndices: \t" + String.join(", ", newSelectedViewIndices.stream().map(x -> x.toString()).collect(Collectors.toList())));
//			System.out.println("SelectedRows: \t" + Arrays.toString(table.getSelectedRows()));
			// update row heights
			updateRowHeights();
			// the next call is needed 
			// because the table does not show the correct status if the filter removes all rows from view 
			updateView();
//			int n = table.getRowCount();
//			for(int i=0; i<n; ++i) {
//				int height = rowHeights[table.convertRowIndexToModel(i)];
//				table.setRowHeight(i, height);
//				rowHeaderColumnTable.setRowHeight(i, height);
//			}
			
		}
		  
	  });
  }
  
  private void addPopupFeature() {
	  this.popupMenu = new JPopupMenu();
	    
	    MouseListener popupListener = new MouseAdapter() {
	      public void mousePressed(MouseEvent e) {
	        maybeShowPopup(e);
	    }

	    public void mouseReleased(MouseEvent e) {
	        maybeShowPopup(e);
	    }

	    private void maybeShowPopup(MouseEvent e) {
	        if (e.isPopupTrigger()) {
	        	if(e.getSource() instanceof JTable) {
	        		SimSearchTable.this.showRowPopUp(e.getComponent(), e.getX(), e.getY());
	        	} else {
	        		SimSearchTable.this.showHeaderPopUp(e.getComponent(), e.getX(), e.getY());
	        	}
	        }
	    }
	    };
	    
	    Arrays.asList(this.table, this.rowHeaderColumnTable).forEach(t -> {
	    	t.addMouseListener(popupListener);
	    	t.getTableHeader().addMouseListener(popupListener);
	    });
	    
  }

  private void addFrozenColumnsResizeFeature() {
	  MouseAdapter mouseAdapter = new MouseAdapter() {
	      private int columnWidthAtMouseDragStart = -1;
	      private int tableWidthAtMouseDragStart = -1;
	      private int mouseDragStartX = -1;
	      private TableColumn tableColumn = null;
	      
	      @Override
	      public void mouseClicked(MouseEvent e) {
	        if(!e.isConsumed()) {
	          if(SwingUtilities.isLeftMouseButton(e)) {
	            // 
	            //SimSearchTable.this.updateSelection();
	            SimSearchTable.this.updateView();
	          }
	        }
	      }

	      @Override
	      public void mouseReleased(MouseEvent e) {
	        // TODO Auto-generated method stub
	        this.tableColumn = null;
//	        System.out.println("mouseReleased");
	      }
	      
	      @Override
	      public void mousePressed(MouseEvent e) {
	    	  if(SwingUtilities.isLeftMouseButton(e)) {
	    		  // this might be the start of an drag operation
	    		  JTableHeader header = (JTableHeader) e.getComponent();
		            int columnIndex = header.columnAtPoint(e.getPoint());
		            this.tableColumn = header.getColumnModel().getColumn(columnIndex);
		            this.tableWidthAtMouseDragStart = SimSearchTable.this.rowHeaderColumnTable.getWidth();
		            this.columnWidthAtMouseDragStart = this.tableColumn.getWidth();
		            this.mouseDragStartX = e.getX();
	    	  }
	        
	      }
	      
	      @Override
	      public void mouseDragged(MouseEvent arg0) {
	        //System.out.println("mouseDragged");
	        if(SwingUtilities.isLeftMouseButton(arg0)) {
	          
	          //int columnIndex = header.
	          
	          if(this.tableColumn==null) {
//	            JTableHeader header = (JTableHeader) arg0.getComponent();
//	            int columnIndex = header.columnAtPoint(arg0.getPoint());
//	            this.tableColumn = header.getColumnModel().getColumn(columnIndex);
//	            this.tableWidthAtMouseDragStart = SimSearchTable.this.rowHeaderColumnTable.getWidth();
//	            this.columnWidthAtMouseDragStart = this.tableColumn.getWidth();
//	            this.mouseDragStartX = arg0.getX();
	          } else {
	            //Dimension dim = new Dimension(this.tableWidthAtMouseDragStart + arg0.getX() - this.mouseDragStartX, SimSearchTable.this.rowHeaderColumnTable.getHeight());
//	            Dimension tmp_min = SimSearchTable.this.rowHeaderColumnTable.getMinimumSize();
//	            Dimension tmp_max = SimSearchTable.this.rowHeaderColumnTable.getMaximumSize();
//	            Dimension tmp_SB = SimSearchTable.this.rowHeaderColumnTable.getSize();
//	            Dimension tmp_PSB = SimSearchTable.this.rowHeaderColumnTable.getPreferredSize();
//	            SimSearchTable.this.rowHeaderColumnTable.setSize(dim);
	            
//	            SimSearchTable.this.rowHeaderColumnTable.setPreferredScrollableViewportSize(dim);
	            int delta = arg0.getX() - this.mouseDragStartX;
	      
	            tableColumn.setWidth(this.columnWidthAtMouseDragStart + delta);
	            delta = tableColumn.getWidth() - this.columnWidthAtMouseDragStart;
	            
	            Dimension dim = new Dimension(this.tableWidthAtMouseDragStart + delta, SimSearchTable.this.rowHeaderColumnTable.getHeight());
	            SimSearchTable.this.rowHeaderColumnTable.setSize(dim);
	            SimSearchTable.this.getRowHeader().setPreferredSize(dim);
	            //tableColumn.setPreferredWidth(this.columnWidthAtMouseDragStart + arg0.getX() - this.mouseDragStartX);
	            //SimSearchTable.this.rowHeaderColumnTable.setCol
	            //Dimension tmp_SA = SimSearchTable.this.rowHeaderColumnTable.getSize();
	            //Dimension tmp_PSA = SimSearchTable.this.rowHeaderColumnTable.getPreferredSize();
	            //System.out.println("Size: " + tmp_SB.toString() + " -> " + tmp_SA.toString());
	            //System.out.println("PSize: " + tmp_PSB.toString() + " -> " + tmp_PSA.toString());
	            //SimSearchTable.this.getRowHeader().setPreferredSize(dim);
	            
	            //SimSearchTable.this.updateTablePosition();
	            //SimSearchTable.this.updateView();
	            //SimSearchTable.this.updateUI();
	          } 
	        }
	      }
	    };
	    this.rowHeaderColumnTable.getTableHeader().addMouseListener(mouseAdapter);
	    this.rowHeaderColumnTable.getTableHeader().addMouseMotionListener(mouseAdapter);
	    
  }
  
  private SimSearchTableModel getModel() {
    return ((this.table==null || this.table.getModel()==null || !(this.table.getModel() instanceof SimSearchTableModel)) ? null :  (SimSearchTableModel) this.table.getModel());
  }
  
  private SimSearchRowSorter getRowSorter() {
    return ((this.table==null || this.table.getRowSorter()==null) ? null :  (SimSearchRowSorter) this.table.getRowSorter());
  }
  
//  private void proccessSelectionEvent(ListSelectionEvent event) {
//    this.selectedModelIndices = new HashSet<>();
//    for(int i=event.getFirstIndex(); i<= event.getLastIndex(); ++i) {
//      if(this.table.isRowSelected(i)) this.selectedModelIndices.add(this.getRowSorter().convertRowIndexToModel(i));
//      //else this.selectedIds.remove(this.getModel().getID(this.getRowSorter().convertRowIndexToModel(i)));
//    }
//  }
  
//  public void updateSelection() {
//    Set<Integer> selectedModelIndices = this.selectedModelIndices.stream().collect(Collectors.toSet());
//    this.table.getSelectionModel().clearSelection();
//    Map<Integer, Integer> modelIndexToViewIndexMap = new HashMap<>();
//    for(int i=0; i<this.table.getRowCount(); ++i) modelIndexToViewIndexMap.put(this.table.getRowSorter().convertRowIndexToModel(i), i);
//    selectedModelIndices = Sets.intersection(selectedModelIndices, modelIndexToViewIndexMap.keySet()).stream().collect(Collectors.toSet());
//    for(Integer id: selectedModelIndices) table.addRowSelectionInterval(modelIndexToViewIndexMap.get(id), modelIndexToViewIndexMap.get(id)); 
//    this.selectedModelIndices = selectedModelIndices;
//  }
  
  private int[] convertViewRowsToModelRows(int[] rows) {
    for(int i=0; i<rows.length; ++i) rows[i] = this.table.getRowSorter().convertRowIndexToModel(rows[i]);
    return rows;
  }
  
  private int convertDropRowToModelRow(int row) {
    return (row>=this.table.getRowCount()?-1:this.table.getRowSorter().convertRowIndexToModel(row));
  }

  protected void updateRowHeader() {
    ((SimSearchJTable.RowHeaderColumnRenderer)  this.rowHeaderColumnTable.getColumnModel().getColumn(0).getCellRenderer()).emptyCache();
    this.table.updateUI();
    this.rowHeaderColumnTable.updateUI();
  }

  private void processInactiveRowFilterEnabledChangedEvent() {
    if(this.getRowSorter()!=null) {
      this.getRowSorter().setInactiveRowFilterEnabled(this.inactiveRowFilterSwitch.isSelected());
//      this.updateSelection();
//      this.updateView();
    }
  }
  
  private void processTextFilterChangedEvent() {

    if(this.filterTextBox==null || this.getRowSorter()==null) return;

    this.applyTextFilterToRowSorter();

//    this.table.updateUI();
//    this.rowHeaderColumnTable.updateUI();
    //this.updateSelection();
    //this.updateView();
    
    //      SimSearchRowSorter rowSorter = (SimSearchRowSorter) this.table.getRowSorter();
    //      this.table.setRowSorter(null);
    //      this.rowHeaderColumnTable.setRowSorter(null);
    //      this.table.setRowSorter(rowSorter);
    //      this.rowHeaderColumnTable.setRowSorter(rowSorter);
  }
  
  private void applyTextFilterToRowSorter() {
    if(this.filterTextBox==null || this.getRowSorter()==null) return;
    
    if(this.useRegexFilterCheckBox!=null && this.useRegexFilterCheckBox.isSelected()) {
      try {
        Pattern pattern = Pattern.compile( this.filterTextBox.getText() );
        this.filterTextBox.setBackground(Color.white);
        this.getRowSorter().setRowFilter(pattern);
        //System.out.println("No PatternSyntaxException");
        this.filterTextBox.setToolTipText("Enter regular expression to filter rows.");
      } catch(PatternSyntaxException e) {
    	 // System.out.println("PatternSyntaxException");
        ((SimSearchRowSorter) this.table.getRowSorter()).setRowFilter("");
        this.filterTextBox.setBackground(Color.RED);
        this.filterTextBox.setToolTipText("The syntax of the regular expression is not valid.");
      }
    } else {
      ((SimSearchRowSorter) this.table.getRowSorter()).setRowFilter(this.filterTextBox.getText());
      this.filterTextBox.setBackground(Color.white);
      this.filterTextBox.setToolTipText("Enter text to filter rows.");
    }
  }


  public void registerRowTextFilter(JTextField filterTextBox, JCheckBox useRegExFilterCheckBox) {

    if(filterTextBox!=null) {

      this.filterTextBox = filterTextBox;
      this.filterColor = this.filterTextBox.getBackground();
      this.filterTextBox.getDocument().addDocumentListener(new DocumentListener() {

        @Override
        public void changedUpdate(DocumentEvent e) {
          SimSearchTable.this.processTextFilterChangedEvent();
        }

        @Override
        public void insertUpdate(DocumentEvent e) {
          SimSearchTable.this.processTextFilterChangedEvent();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
          SimSearchTable.this.processTextFilterChangedEvent();
        }

      });
      
      if(useRegExFilterCheckBox!=null) {
        this.useRegexFilterCheckBox = useRegExFilterCheckBox;

        this.useRegexFilterCheckBox.addChangeListener(new ChangeListener()  {
          @Override
          public void stateChanged(ChangeEvent arg0) {
            SimSearchTable.this.processTextFilterChangedEvent();
          }
        }
            );   

      }
    }
  }
  
  public void registerInactiveRowFilterSwitch(AbstractButton inactiveRowFilterSwitch) {
    if(inactiveRowFilterSwitch==null) return;
    this.inactiveRowFilterSwitch = inactiveRowFilterSwitch;
    inactiveRowFilterSwitch.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent arg0) {
        // TODO Auto-generated method stub
        SimSearchTable.this.processInactiveRowFilterEnabledChangedEvent();
        //SimSearchJFrame.this.table.setInactiveRowFilterEnabled(!((JCheckBoxMenuItem) arg0.getSource()).isSelected());
      }

    });
  }
  
  public void registerSimSetIgnoreButtons(JButton ignoreSimSetButton, JButton ignoreAllPairsInSimSetButton) {
	  if(ignoreSimSetButton==null) return;
	  if(ignoreAllPairsInSimSetButton==null) return;
	  this.ignoreSimSetButton = ignoreSimSetButton;
	  this.ignoreAllPairsInSimSetButton = ignoreAllPairsInSimSetButton;
	  ActionListener actionListener = new ActionListener() {

		  @Override
		  public void actionPerformed(ActionEvent arg0) {
			  SimSearchTable.this.processIgnoreButtonEvent((JButton) arg0.getSource());
		  };
	  };
	  this.ignoreSimSetButton.addActionListener(actionListener);
	  this.ignoreAllPairsInSimSetButton.addActionListener(actionListener);
  }

  private void processIgnoreButtonEvent(JButton ignoreButton) {
	//boolean[] rowIsMissing = new boolean[this.getModel().getRowCount()];
	//Arrays.fill(rowIsMissing, true);
	//for(int row=0; row<this.table.getRowCount(); ++row) rowIsMissing[this.table.convertRowIndexToModel(row)] = false;
	  int n = this.getModel().getRowCount();
	for(int row=0; row<n; ++row) if(this.table.convertRowIndexToView(row)<0 && !this.getModel().isMerged(row))
		if(JOptionPane.showConfirmDialog(this, "Not all rows are shown in the view (filter is active). Proceed nethertheless?","Confirm dissimilarity mark",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE)!=JOptionPane.YES_OPTION) return;
	
    if(ignoreButton == this.ignoreSimSetButton) this.getModel().ignoreSimSet();
    else if(ignoreButton == this.ignoreAllPairsInSimSetButton) this.getModel().ignoreAllPairsInSimSet();
  }

  
  protected ViewSettings getViewSettings() {
	  return this.viewSettings;
  }
  
  public boolean isSimSetIgnored() {
    return (this.getModel()==null?false:this.getModel().isSimSetIgnored());
  }
  
  public boolean isSimSetIgnoreAvailable() {
    return (this.getModel()==null?false:this.getModel().isSimSetIgnoreAvailable());
  }
  
  public boolean isMergeValid(int[] rowsToMerge, int rowToMergeTo) {
    rowsToMerge = convertViewRowsToModelRows(rowsToMerge);
    rowToMergeTo = convertViewRowsToModelRows(new int[] {rowToMergeTo})[0];
    return ((SimSearchTableModel) this.table.getModel()).isMergeValid(rowsToMerge, rowToMergeTo);
  }
  
  
  public void mergeRows(int[] rowsToMerge, int rowToMergeTo) {
    rowsToMerge = convertViewRowsToModelRows(rowsToMerge);
    rowToMergeTo = convertViewRowsToModelRows(new int[] {rowToMergeTo})[0];
    this.getModel().mergeRows(rowsToMerge, rowToMergeTo);
    //((SimSearchRowSorter) this.table.getRowSorter()).sort();
    //this.updateSelection();
    //this.updateView();
  }

  public boolean isRowMoveValid(int[] rowsToMove, int rowToMoveBefore) {
    //System.out.println("RowsToMove: " + Arrays.toString(rowsToMove));
    //System.out.println("RowToMoveBefore: " + rowToMoveBefore);
    rowsToMove = convertViewRowsToModelRows(rowsToMove);
    rowToMoveBefore = convertDropRowToModelRow(rowToMoveBefore);
    return this.getRowSorter().isRowMoveValid(rowsToMove, rowToMoveBefore);
  }
  
  public void moveRows(int[] rowsToMove, int rowToMoveBefore) {
    rowsToMove = convertViewRowsToModelRows(rowsToMove);
    rowToMoveBefore = convertDropRowToModelRow(rowToMoveBefore);
    this.getRowSorter().moveRows(rowsToMove, rowToMoveBefore);
//    this.updateSelection();
//    this.updateView();
  }
  
//  public void setInactiveRowFilterEnabled(boolean value) {
//    if(this.getRowSorter()!=null && this.getRowSorter().isInactiveRowFilterEnabled()!=value) {
//      this.getRowSorter().setInactiveRowFilterEnabled(value);
//      this.updateView();
//      this.updateSelection();
//    }
//  }
  
  private boolean isInactiveRowFilterEnabled() { return (this.inactiveRowFilterSwitch==null?false:this.inactiveRowFilterSwitch.isSelected()); }
 
  public void updateView() {
    ((RowHeaderColumnRenderer) this.rowHeaderColumnTable.getColumnModel().getColumn(0).getCellRenderer()).emptyCache();
    this.table.updateUI();
    this.rowHeaderColumnTable.updateUI();
  }

//  public void loadData(SimSearchTableModel tableModel) {
//    this.loadData(tableModel, this.viewSettings);
//  }
  
  private List<Integer> loadColumnSettings() { //SimSearchTableModel tableModel) {
    
    SimSearchTableModel tableModel = this.getModel();
    SimSearch.SimSet simSet = tableModel.getSimSet();
    
    this.frozenColumns = new ArrayList<>();
    this.invisibleColumns = new HashSet<>();

    List<String> availableColumns = new ArrayList<>(tableModel.getColumnNames());
    availableColumns.remove(0); // Status
    
    List<String> frozenColumns = new ArrayList<>(); //availableColumns.stream().collect(Collectors.toList());
    if(this.viewSettings.frozenColumns.containsKey(simSet.getType())) {
    	frozenColumns = new ArrayList<>(this.viewSettings.frozenColumns.get(simSet.getType()));  
    	frozenColumns.retainAll(availableColumns);
    }
    
    Set<String> invisibleColumns = new HashSet<>();
    if(this.viewSettings.invisibleColumns.containsKey(simSet.getType())) {
    	invisibleColumns = new HashSet<>(this.viewSettings.invisibleColumns.get(simSet.getType()));
    	invisibleColumns.retainAll(availableColumns);
    }
//    
    Set<String> visibleNotFrozenColumns = new HashSet<>(availableColumns);
    visibleNotFrozenColumns.removeAll(invisibleColumns);
    visibleNotFrozenColumns.removeAll(frozenColumns);
//    
    List<String> columnOrder = new ArrayList<>();
    if(this.viewSettings.columnOrder.containsKey(simSet.getType())) {
    	columnOrder = new ArrayList<>(this.viewSettings.columnOrder.get(simSet.getType()));
    	columnOrder.retainAll(visibleNotFrozenColumns);
    }
    
    
    for(String columnName: frozenColumns) this.frozenColumns.add(availableColumns.indexOf(columnName)+1);
    for(String columnName: invisibleColumns) this.invisibleColumns.add(availableColumns.indexOf(columnName)+1);
    
    List<Integer> columnSorting = new ArrayList<>();
    for(String columnName: columnOrder) columnSorting.add(availableColumns.indexOf(columnName)+1);
    
    for(int column=1; column<tableModel.getColumnCount(); ++column) 
      if(!this.invisibleColumns.contains(column) && 
          !this.frozenColumns.contains(column) && !columnSorting.contains(column)) columnSorting.add(column); 
//    
    return columnSorting;
  }
  
//  private void applyColumnAnRowSettingsToView() {
//    // applyColumnWidth
//    
//    SimSearchTableModel tableModel = this.getModel();
//    SimSearch.SimSet simSet = tableModel.getSimSet();
//    
//    Map<Integer, Integer> columnWidths = this.viewSettings.columnWidths.get(simSet);
//    Arrays.asList(this.rowHeaderColumnTable, this.table).forEach(table -> {
//      for(int column=(table==this.table?0:1); column<table.getColumnCount(); ++column) {
//    	int modelIndex = table.convertColumnIndexToModel(column);
//        if(columnWidths!=null && columnWidths.containsKey(modelIndex))
//          table.getColumnModel().getColumn(column).setWidth(columnWidths.get(modelIndex));
//        else
//          table.getColumnModel().getColumn(column).sizeWidthToFit();
//      }
//    });
//    
//    // applyRowHeight (on unfiltered rows)
//    Map<Integer, Integer> rowHeights = this.viewSettings.rowHeights.get(simSet.getType());
//    if(rowHeights==null) rowHeights = new HashMap<>();
//    for(int row=0; row<this.table.getRowCount(); ++row) {
//      int id = tableModel.getID(this.table.convertRowIndexToModel(row));
//      if(rowHeights.containsKey(id))
//        //this.table.
//        setRowHeight(row, rowHeights.get(id));
//        
//      else {
//        //this.table.
//        Random rnd = new Random();
//        setRowHeight(row, (int) Math.round((rnd.nextInt(5)+1)*0.5*ViewSettings.DEFAULT_ROW_HEIGHT));
//      }
//    }
//    //if(!this.viewSettings.rowHeights.containsKey(simSet.getType()))
//  }
  
  private void applyColumnSettingsToView() {
    // applyColumnWidth
    
    SimSearchTableModel tableModel = this.getModel();
    SimSearch.SimSet simSet = tableModel.getSimSet();
    
    Map<Integer, Integer> columnWidths = this.viewSettings.columnWidths.get(simSet);
    
    Arrays.asList(this.rowHeaderColumnTable, this.table).forEach(table -> {
      for(int column=(table==this.table?0:1); column<table.getColumnCount(); ++column) {
        int modelIndex = table.convertColumnIndexToModel(column);
        if(columnWidths!=null && columnWidths.containsKey(modelIndex))
          table.getColumnModel().getColumn(column).setPreferredWidth(columnWidths.get(modelIndex));
        else
          table.adjustColumnWidth(column); //           getColumnModel().getColumn(column).sizeWidthToFit();
      }
    });
  }
  
  private void applyRowSettingsToView() {
	  SimSearchTableModel tableModel = this.getModel();
	  SimSearch.SimSet simSet = tableModel.getSimSet();
	  Map<Integer,Integer> idToRowHeight = viewSettings.rowHeights.get(simSet.getType());
	  rowHeights = new int[tableModel.getRowCount()];
	  int nRows = tableModel.getRowCount();
	  for(int row=0; row<nRows; ++row) {
		  Integer rowHeight = (idToRowHeight==null?null:idToRowHeight.get(tableModel.getID(row)));
		  if(rowHeight!=null) rowHeights[row] = rowHeight;
		  else rowHeights[row] = getPreferredRowHeight(row);
	  }
	  
	  this.rowHeaderColumnTable.setRowHeights(rowHeights);
	  this.updateRowHeights();
  }
  
  private int getPreferredRowHeight(int row) {
	  int height = ViewSettings.ROW_HEIGHT_MIN;
	  for(SimSearchJTable table : Arrays.asList(this.table, this.rowHeaderColumnTable)) {
		  height = Math.max(height, table.getPreferredRowHeight(row));
	  }
	  return height;
  }
//  private void loadRowHeights() {
//    SimSearchTableModel tableModel = this.getModel();
//    SimSearch.SimSet simSet = tableModel.getSimSet();
//    
//    this.rowHeights = new int[tableModel.getRowCount()];
//    Map<Integer, Integer> rowHeights = this.viewSettings.rowHeights.get(simSet.getType());
//    if(rowHeights==null) rowHeights = new HashMap<>();
//    Random rnd = new Random();
//    for(int row=0; row<tableModel.getRowCount(); ++row) {
//      Integer w = rowHeights.get(tableModel.getID(row));
//      this.rowHeights[row] = (w!=null?w:ViewSettings.ROW_HEIGHT_DEFAULT); //(int) Math.round((rnd.nextInt(5)+1)*0.5*ViewSettings.DEFAULT_ROW_HEIGHT));
//    }
//  }
  
//  private void applyRowSettingsToView() {
//    
//    for(int row=0; row<this.table.getRowCount(); ++row) setRowHeight(row, this.rowHeights[this.table.convertRowIndexToModel(row)]);
//  }
  
//  private void setRowHeight(int row, int height) {
//    EventQueue.invokeLater(new Runnable() {
//      @Override public void run() {
//        table.setRowHeight(row, height);
//        rowHeaderColumnTable.setRowHeight(row, height);
//      }
//    });
//  }
  
  private void updateRowHeights() {
	    EventQueue.invokeLater(new Runnable() {
	      @Override public void run() {
	    	  int n = table.getRowCount();
	    	  for(int viewIndex=0; viewIndex<n; ++viewIndex) {
	    		  int modelIndex = table.convertRowIndexToModel(viewIndex);
	    		  table.setRowHeight(viewIndex, rowHeights[modelIndex]);
	  	          rowHeaderColumnTable.setRowHeight(viewIndex, rowHeights[modelIndex]);
	    	  }
	        
	      }
	    });
	  }
  
  private void applyRowSortingToView() {
    SimSearchTableModel tableModel = this.getModel();
    SimSearch.SimSet simSet = tableModel.getSimSet();
    
    // applyRowSorting
    if(this.viewSettings.rowOrder.containsKey(simSet)) {
//      Map<String, Integer> visibleColumns = new HashMap<>(); 
//      Arrays.asList(this.table, this.rowHeaderColumnTable).forEach(table -> {
//        for(int column=0; column<table.getColumnCount(); ++column) { visibleColumns.put(table.getColumnName(column), table.convertColumnIndexToModel(column)); }
//      });
//      List<SortKey> sortKeys = new ArrayList<>();
//      for(Entry<String, SortOrder> entry: this.viewSettings.sortingKeys.get(simSet)) {
//        if(visibleColumns.containsKey(entry.getKey())) sortKeys.add(new SortKey(visibleColumns.get(entry.getKey()), entry.getValue()));
//      }
      this.getRowSorter().applySorting(this.viewSettings.rowOrder.get(simSet), this.viewSettings.sortKeys.get(simSet));
    } else {
      //No sorting predefined
      this.getRowSorter().setSortKeys(new ArrayList<>(Arrays.asList(new SortKey(0, SortOrder.DESCENDING))));
    }
  }
  
  public void applySettingsFromView() {
	SimSearchTableModel tableModel = this.getModel();
	SimSearch.SimSet simSet = tableModel.getSimSet();
	
    this.viewSettings.showRemovedObjects = !this.inactiveRowFilterSwitch.isSelected();
    List<String> columnOrder = new ArrayList<>();
    for(int column=0; column<this.table.getColumnCount(); ++column) columnOrder.add(this.table.getColumnName(column));
    this.viewSettings.columnOrder.put(simSet.getType(), columnOrder);
    List<String> frozenColumns = new ArrayList<>();
    for(int column=1; column<this.rowHeaderColumnTable.getColumnCount(); ++column) frozenColumns.add(this.rowHeaderColumnTable.getColumnName(column));
    this.viewSettings.frozenColumns.put(simSet.getType(), frozenColumns);
    
    Set<String> invisibleColumns = new HashSet<>();
    for(int column: this.invisibleColumns) invisibleColumns.add(tableModel.getColumnName(column));
    this.viewSettings.invisibleColumns.put(simSet.getType(), invisibleColumns);
    
    this.viewSettings.rowOrder.put(simSet, this.getRowSorter().getIdOrder());
    this.viewSettings.sortKeys.put(simSet, new ArrayList<>(this.getRowSorter().getSortKeys()));
    
    Map<Integer, Integer> columnWidths = new HashMap<>(); //this.viewSettings.columnWidths.get(simSet);
    for(JTable table: Arrays.asList(this.table, this.rowHeaderColumnTable)) {
      for(int column=(table==this.table?0:1); column<table.getColumnCount(); ++column) {
        columnWidths.put(table.convertColumnIndexToModel(column), table.getColumnModel().getColumn(column).getPreferredWidth());
      }
    }
    this.viewSettings.columnWidths.put(simSet, columnWidths);
    Map<Integer, Integer> rowHeights = this.viewSettings.rowHeights.get(simSet.getType());
    if(rowHeights==null) rowHeights = new HashMap<>();
    for(int row=0; row<tableModel.getRowCount(); ++row) rowHeights.put(tableModel.getID(row), this.rowHeights[row]);
    this.viewSettings.rowHeights.put(simSet.getType(), rowHeights);
    //List<Integer> idOrder = this.getRowSorter().getIdOrder();
  }
  
  private void addColumns() {
    List<Integer> columnSorting = this.loadColumnSettings();
    
    this.table.removeColumns();
    for(Integer column: columnSorting) this.table.addColumn(new TableColumn(column));
    
    this.rowHeaderColumnTable.removeColumns();
    this.rowHeaderColumnTable.addColumn(new TableColumn(0));
    for(Integer column: frozenColumns) this.rowHeaderColumnTable.addColumn(new TableColumn(column));
  }
  
  public void loadData(SimSearchTableModel tableModel) {
    //List<Integer> columnSorting = this.loadColumnSettings(tableModel);
    List<Integer> selectedIds = new ArrayList<>();
    if(this.getModel()!=null) {
      this.applySettingsFromView();
      if(tableModel!=null && tableModel.getSimSet()==this.getModel().getSimSet()) selectedIds = this.table.getSelectedIds();
    }
    
    if (tableModel!=null) {
      //this.table.removeColumns();
      //this.rowHeaderColumnTable.removeColumns();
      this.table.setModel(tableModel);
      this.rowHeaderColumnTable.setModel(tableModel);
      
      //this.loadRowHeights();
    
      this.addColumns();
      Arrays.asList(this.table, this.rowHeaderColumnTable).forEach(table -> {
        table.initColumnRenderers();
        table.updateUI();
      });
      
      this.resetRowSorter(tableModel);
  //
      // Put it in a viewport that we can control a bit
      JViewport jv = new JViewport();
      jv.setView(this.rowHeaderColumnTable);
      Dimension dim = this.rowHeaderColumnTable.getMaximumSize();
      jv.setPreferredSize(this.rowHeaderColumnTable.getMaximumSize());
      this.setRowHeader(jv);
  //
      this.setCorner(ScrollPaneConstants.UPPER_LEFT_CORNER, this.rowHeaderColumnTable
          .getTableHeader());
  //    this.simSetIgnoreSwitch.removeActionListener(this.simSetIgnoreSwitchActionListener);
  //    this.simSetIgnoreSwitch.setSelected(tableModel.isSimSetIgnored());
  //    this.simSetIgnoreSwitch.addActionListener(this.simSetIgnoreSwitchActionListener);
  //    
  //    
  ////    this.rowHeaderColumnModel.getColumn(1).setResizable(true);
  ////    int tmp_min = this.rowHeaderColumnModel.getColumn(1).getMinWidth();
  ////    int tmp_max = this.rowHeaderColumnModel.getColumn(1).getMaxWidth();
  ////    Dimension dim_max = this.rowHeaderColumnTable.getMaximumSize();
  ////    Dimension dim_min = this.rowHeaderColumnTable.getMinimumSize();
  ////    this.rowHeaderColumnTable.getTableHeader().setResizingAllowed(true);
      this.table.applyIdSelection(selectedIds);
      //this.selectedModelIndices.clear();
      this.setBorderTitle(tableModel.getSimSet().getType());
      //this.table.setRowHeight(2,30);
    } 
  }
  
  
  private void resetRowSorter(SimSearchTableModel tableModel) {
	  SimSearchRowSorter rowSorter = new SimSearchRowSorter(tableModel);
      this.updateVisibleSorterColumns(rowSorter);
//      rowSorter.addRowSorterListener(new RowSorterListener() {
//        @Override
//        public void sorterChanged(RowSorterEvent e) {
//            SimSearchTable.this.processSortingChangedEvent();
//        }
//    });
      
      //rowSorter.setInactiveRowFilterEnabled(this.isInactiveRowFilterEnabled());
      
      table.setRowSorter(rowSorter);
      this.rowHeaderColumnTable.setRowSorter(rowSorter);
      
      this.applyRowSettingsToView();
      this.applyColumnSettingsToView();
  //    
      rowSorter.setInactiveRowFilterEnabled(this.isInactiveRowFilterEnabled());
      if(this.filterTextBox.getText()!=null && !this.filterTextBox.getText().isEmpty()) this.applyTextFilterToRowSorter();
  //    
      this.applyRowSortingToView();
      
      this.addSelectionAndRowHeightUpdateOnSortFix(rowSorter);
//      rowSorter.addRowSequenceChangedListener(new SimSearchRowSorter.RowSequenceChangedListener() {
//
//		@Override
//		public void RowSequenceChanged(List<Integer> oldViewToModel) {
//			// TODO Auto-generated method stub
//			
//		}
//    	  
//      });
  }
//  private void processSortingChangedEvent() {
//    this.applyRowSettingsToView();  
//  }
  
  public void setBorderTitle(String text) {
    if(text==null || text.isEmpty()) text = "Results";
    if(this.getBorder()==null || !(this.getBorder() instanceof TitledBorder)) {
      this.setBorder(BorderFactory.createTitledBorder(text));
    } else {
      ((TitledBorder) this.getBorder()).setTitle(text);
    }
  }
  
  public void updateRowHeightMenu(JMenu menu, boolean addShortCuts) {
    if(this.getModel()==null) {
      menu.setEnabled(false);
    } else {
      //menu.removeAll();
      menu.setEnabled(true);
    }
//    if(menu.getComponentCount()==0) {
//      
//    }
  }
  
  public void updateFontSizeMenu(JMenu menu) {
    if(this.getModel()==null) {
      menu.setEnabled(false);
    } else {
      menu.removeAll();
      menu.setEnabled(true);
      JMenuItem fontSizeIncrease = new JMenuItem("Increase");
      fontSizeIncrease.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent arg0) {
          SimSearchTable.this.processUserChangeFontSizeRequest(+1);
        }
      });
      
      fontSizeIncrease.getAccessibleContext().setAccessibleDescription(
          "Increase table font size.");
      menu.add(fontSizeIncrease);
      JMenuItem fontSizeDecrease = new JMenuItem("Decrease");
      fontSizeDecrease.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent arg0) {
          SimSearchTable.this.processUserChangeFontSizeRequest(-1);
        }
      });
      menu.add(fontSizeDecrease);
    }
  }
  
  private void setBorderTitle(SimSearch.SimSet.Type simSetType) {
    switch(simSetType) {
      case STATION:
        setBorderTitle("Similar stations:");
        break;
      case PRODUCT:
        setBorderTitle("Similar products:");
      case LOT:
        setBorderTitle("Similar lots:");
        break;
      case DELIVERY:
        setBorderTitle("Similar deliveries:");
        break;
      default:
        setBorderTitle((String) null);
    }
  }
  
  public void clear() {
	this.table.setRowSorter(null);
	this.rowHeaderColumnTable.setRowSorter(null);
    this.table.setModel(null);
    this.rowHeaderColumnTable.setModel(this.table.getModel());
    this.setBorderTitle("");
    this.inactiveRowFilterSwitch.setEnabled(false);
    this.filterTextBox.setEnabled(false);
    if(useRegexFilterCheckBox!=null) this.useRegexFilterCheckBox.setEnabled(false);
    this.ignoreAllPairsInSimSetButton.setEnabled(false);
    this.ignoreSimSetButton.setEnabled(false);
  }

  private void mouseClickedOnTableHeader(MouseEvent e) {
    if(e.getClickCount()==2 && e.getButton()==MouseEvent.BUTTON1) {
      JTableHeader tableHeader = (JTableHeader) e.getSource();
      // search TableColumn and resize it

      //          int columnIndex = this.table.getTableHeader().columnAtPoint(e.getPoint()); 
      //          Component comp = tableHeader.getComponentAt(e.getPoint());
      //          
      System.out.println("Mouse double clicked on " + e.getSource().toString()+"\n");
      //          System.out.println("Mouse double clicked on " + comp.toString() +"\n");
    }
  }
  
  private class ColumnVisibilityCheckBoxMenuItem extends JCheckBoxMenuItem {
//    private StayOpenCheckBoxMenuItemUI stayOpenMenuItemUI;
//    private MenuItemUI wrappedMenuItemUI;
    
//    private static class StayOpenCheckBoxMenuItemUI extends BasicCheckBoxMenuItemUI {
//      private MenuItemUI wrappedMenuItemUI;
//
//      @Override
//      protected void doClick(MenuSelectionManager msm) {
//         menuItem.doClick(0);
////         this.paint(g, c);
////         this.paintBackground(g, menuItem, bgColor);
//      }
//      
//      @Override
//      public void update(Graphics g, JComponent c) {
//        if(wrappedMenuItemUI!=null) wrappedMenuItemUI.update(g, c);
//      }
//      
//      @Override
//      public void paint(Graphics g, JComponent c) {
//        if(wrappedMenuItemUI!=null) wrappedMenuItemUI.paint(g, c);
//      }
//      
////      @Override
////      public void paintBackground(Graphics g, JMenuItem menuItem, Color bgColor) {
////        if(menuItemUI!=null) menuItemUI.p
////      }
//
//      public static ComponentUI createUI(JComponent c) {
//         return null; //new StayOpenCheckBoxMenuItemUI();
//      } 
//   }
    
    private ColumnVisibilityCheckBoxMenuItem(String text, int column) {
      super(text);

      super.addMouseListener(new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
          if(SwingUtilities.isLeftMouseButton(e)) {
            ColumnVisibilityCheckBoxMenuItem.this.setSelected(!ColumnVisibilityCheckBoxMenuItem.this.isSelected());
            SimSearchTable.this.setColumnVisible(column, ColumnVisibilityCheckBoxMenuItem.this.isSelected());
          }
        }
      });
//      this.stayOpenMenuItemUI = new StayOpenCheckBoxMenuItemUI();
//      this.stayOpenMenuItemUI.wrappedMenuItemUI = wrappedMenuItemUI;
//      super.setUI(this.stayOpenMenuItemUI);
    }
    

    
    public void addMouseListener(MouseListener listener) {
      Class<?> declaringClass = null;
      try {
        declaringClass = listener.getClass().getDeclaringClass();
      } catch(Exception err) {}
      
      if(declaringClass==null || !MenuItemUI.class.isAssignableFrom(declaringClass)) {
      //if(declaringClass==null && !declaringClass.equals(BasicMenuItemUI.class)) {
        super.addMouseListener(listener);
      }
    }
    
//    public void setUI(MenuItemUI ui) {
//      this.wrappedMenuItemUI = ui;
//      if(this.stayOpenMenuItemUI!=null) this.stayOpenMenuItemUI.wrappedMenuItemUI = ui;
//      //this.stayOpenMenuItemUI.menuItemUI = ui;
//    }
  }
  
//  private class SwitchColumnVisibilityActionListener implements ActionListener{
//    private final int column;
//    SwitchColumnVisibilityActionListener(int column) { this.column = column; }
//    
//    @Override
//    public void actionPerformed(ActionEvent e) {
//      SimSearchTable.this.setColumnVisible(column, ((JCheckBoxMenuItem) e.getSource()).isSelected());
//    }
//  }
  
  class Repainter implements PropertyChangeListener {
	  public void propertyChange(PropertyChangeEvent pce) {
		  //pce.
		  repaintDropLocation(pce.getOldValue());
		  repaintDropLocation(pce.getNewValue());
	  }
  }
  
  private void repaintDropLocation(Object object) {
	  this.table.updateUI();
	  this.rowHeaderColumnTable.updateUI();
//	  JTable.DropLocation dropLocation = this.table.getDropLocation();
//	  if(dropLocation==null) dropLocation = this.rowHeaderColumnTable.getDropLocation();
//	  if(dropLocation!=null) {
//		  
//		  
//		  
//		  if(dropLocation.isInsertRow()) System.out.println("Insert Row");
//		  else if(!dropLocation.isInsertColumn()) {
//			  this.table.updateUI();
//			  this.rowHeaderColumnTable.updateUI();
////			  BasicTableUI tableUI = (BasicTableUI) this.table.getUI();
////			  tableUI.
////			  int k=6;
//		  }
//	  }
  }
  
  private void repaintDropLocation(MouseEvent e) {
	  //if(this.table.getRow)
  }
  
  private void repaintDropLocation(int column) {
	  
  }
  
  public void processUserChangeRowHeightRequest(int rowHeightDelta) {
    if(this.getModel()==null) return;
    if(rowHeightDelta>0) this.table.setRowHeight(Math.min(this.table.getRowHeight() + rowHeightDelta, ViewSettings.ROW_HEIGHT_MAX));
    else if (rowHeightDelta<0) this.table.setRowHeight(Math.max(this.table.getRowHeight() + rowHeightDelta, ViewSettings.ROW_HEIGHT_MIN));
    if(this.table.getRowHeight()!=this.rowHeaderColumnTable.getRowHeight()) this.rowHeaderColumnTable.setRowHeight(this.table.getRowHeight());
  }
  
  private void processUserChangeFontSizeRequest(int fontSizeDelta) {
    Font font = this.table.getFont();
    
    if(fontSizeDelta>0) font = new Font(font.getFontName(), font.getStyle(),Math.min(font.getSize()+fontSizeDelta, ViewSettings.FONT_SIZE_MAX));
    if(fontSizeDelta<0) font = new Font(font.getFontName(), font.getStyle(),Math.max(font.getSize()+fontSizeDelta, ViewSettings.FONT_SIZE_MIN));
    
    if(font!=this.table.getFont()) {
      this.table.setFont(font);
      this.rowHeaderColumnTable.setFont(font);
    }
    
  }
  
  private static class CopyAdapter implements ActionListener {
	  
	  private JTable rowHeader;
	  private JTable table;
	  
	  private CopyAdapter(JTable table, JTable rowHeader) {
		  //jTable1 = myJTable;
		  this.rowHeader = rowHeader;
		  this.table = table;
	      KeyStroke copy = KeyStroke.getKeyStroke(KeyEvent.VK_C,ActionEvent.CTRL_MASK,false);
	      // Identifying the copy KeyStroke user can modify this
	      // to copy on some other Key combination.
	      //KeyStroke paste = KeyStroke.getKeyStroke(KeyEvent.VK_V,ActionEvent.CTRL_MASK,false);
	      // Identifying the Paste KeyStroke user can modify this
	      //to copy on some other Key combination.
	      table.registerKeyboardAction(this,"Copy",copy,JComponent.WHEN_FOCUSED);
	      rowHeader.registerKeyboardAction(this,"Copy",copy,JComponent.WHEN_FOCUSED);
	      //system = Toolkit.getDefaultToolkit().getSystemClipboard();
	  }

	@Override
	public void actionPerformed(ActionEvent e) {
		
		JTable tableToCopyFrom = (JTable) e.getSource();
		
		int[] rowsToCopyFrom = tableToCopyFrom.getSelectedRows();
        int colToCopyFrom = tableToCopyFrom.getSelectedColumn();
        if(rowsToCopyFrom.length>0) this.copyData(tableToCopyFrom, rowsToCopyFrom, colToCopyFrom);
        
	}
	
	public void copyData(JTable tableToCopyFrom, int[] rowIndices, int columnIndex ) {
		Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();
		List<String> rowTextList = new ArrayList<>();
        for(int row : rowIndices) {
        	List<String> cellTextList = new ArrayList<>();
        	if(tableToCopyFrom==rowHeader && columnIndex==0) {
        		// full row copy
        		for(JTable table : Arrays.asList(rowHeader, table)) {
        			for(int col=(table==rowHeader?1:0); col<table.getColumnCount(); ++col) {
        				Object value = table.getValueAt(row,col);
        				cellTextList.add((value==null?"":value.toString()));
        			}
        		}
        	} else {
        		// cell copy
        		Object value = tableToCopyFrom.getValueAt(row,columnIndex);
        		cellTextList.add((value==null?"":value.toString()));
        	}
        	rowTextList.add(String.join("\t", cellTextList));
        }
        
        StringSelection strSel = new StringSelection(String.join("\n", rowTextList));
        cb.setContents(strSel, strSel);
	}
	  
  }

}
