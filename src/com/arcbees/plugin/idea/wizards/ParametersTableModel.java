package com.arcbees.plugin.idea.wizards;

import com.arcbees.plugin.idea.domain.ParameterModel;

import javax.swing.table.AbstractTableModel;
import java.util.List;

/**
 * Created by serg on 21.08.2014.
 */
public class ParametersTableModel extends AbstractTableModel {

    private String[] colNames = new String[]{ "Type", "Name" };

    private List<ParameterModel> parameterModels;

    /**
     * Returns the number of rows in the model. A
     * <code>JTable</code> uses this method to determine how many rows it
     * should display.  This method should be quick, as it
     * is called frequently during rendering.
     *
     * @return the number of rows in the model
     * @see #getColumnCount
     */
    @Override
    public int getRowCount() {
        return parameterModels != null ? parameterModels.size() : 0;
    }

    /**
     * Returns the number of columns in the model. A
     * <code>JTable</code> uses this method to determine how many columns it
     * should create and display by default.
     *
     * @return the number of columns in the model
     * @see #getRowCount
     */
    @Override
    public int getColumnCount() {
        return 2;
    }

    /**
     * Returns the value for the cell at <code>columnIndex</code> and
     * <code>rowIndex</code>.
     *
     * @param rowIndex    the row whose value is to be queried
     * @param columnIndex the column whose value is to be queried
     * @return the value Object at the specified cell
     */
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        ParameterModel parameterModel = parameterModels.get(rowIndex);

        if (columnIndex == 0)
            return parameterModel.getType();

        return parameterModel.getName();
    }

    /**
     * Returns a default name for the column using spreadsheet conventions:
     * A, B, C, ... Z, AA, AB, etc.  If <code>column</code> cannot be found,
     * returns an empty string.
     *
     * @param column the column being queried
     * @return a string containing the default name of <code>column</code>
     */
    @Override
    public String getColumnName(int column) {
        return colNames[column];
    }

    public List<ParameterModel> getParameterModels() {
        return parameterModels;
    }

    public void setParameterModels(List<ParameterModel> parameterModels) {
        this.parameterModels = parameterModels;
        fireTableDataChanged();
    }
}
