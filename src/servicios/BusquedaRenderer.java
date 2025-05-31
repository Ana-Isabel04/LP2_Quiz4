package servicios;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class BusquedaRenderer extends DefaultTableCellRenderer {
    private String textoBuscar = "";

    public void setTextoBuscar(String textoBuscar) {
        this.textoBuscar = textoBuscar.toLowerCase();
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {

        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        if (value != null && textoBuscar != null && !textoBuscar.isEmpty()) {
            String textoCelda = value.toString().toLowerCase();
            if (textoCelda.contains(textoBuscar)) {
                c.setBackground(Color.YELLOW);
            } else {
                c.setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
            }
        } else {
            c.setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
        }

        return c;
    }
}
