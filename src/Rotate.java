import com.itextpdf.text.pdf.PdfNumber;
import com.itextpdf.text.pdf.PdfPage;
import com.itextpdf.text.pdf.PdfPageEventHelper;

public class Rotate extends PdfPageEventHelper {
		  
	        protected PdfNumber orientation = PdfPage.PORTRAIT;
	 
	        public void setOrientation(PdfNumber orientation) {
	            this.orientation = orientation;
	        }
	
	  }