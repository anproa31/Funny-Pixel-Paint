package paintpix;

import java.awt.Color;

import javax.swing.*;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

@SuppressWarnings("serial")
public class ColorPicker extends JColorChooser{
	private MainController controller;
	
	private class Listener implements ChangeListener {

		@Override
		public void stateChanged(ChangeEvent e) {
			ColorToggler colorToggler = ColorPicker.this.controller.getColorToggler();
			
			if (colorToggler == null)
				return;
			
			colorToggler.setColor(ColorPicker.this.getColor());
		}
		
	}
	
	public ColorPicker(Color initialColor){
		super(initialColor);
		this.setPreviewPanel(new JPanel());

		// removing Panel except swatches and RGB
        for(AbstractColorChooserPanel p: this.getChooserPanels())
        {
            String displayName = p.getDisplayName();

            if (displayName.equals("CMYK")) {
                this.removeChooserPanel(p);
            }
        }

	    this.getSelectionModel().addChangeListener(new Listener()); 
	    this.setBorder(BorderFactory.createTitledBorder(
            "Color Picker"));
	}

	public void setController(MainController controller)
	{
		this.controller = controller;
	}
	
	
}
