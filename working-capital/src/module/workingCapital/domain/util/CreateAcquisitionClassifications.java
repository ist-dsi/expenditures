package module.workingCapital.domain.util;

import module.workingCapital.domain.AcquisitionClassification;
import myorg.domain.scheduler.WriteCustomTask;

public class CreateAcquisitionClassifications extends WriteCustomTask {

    @Override
    public void doIt() {
	create("Combustiveis", "622121", "020102");
	create("Laboratorio", "622151", "020120");
	create("Ferra.Utensílios", "622159", "020117");
	create("Livros", "62216", "020118");
	create("Mat.Escritorio", "622171", "020108");
	create("C.Informa", "622172", "020108");
	create("Cons.Rep", "622323", "020203");
	create("Trab. Espec.", "622369", "020220");
	create("Correio", "622221", "020209");
	create("Transportes", "622271", "020213");
	create("Alim. Aloj.", "622272", "020213");
	create("Out. Fornec.", "622981", "020121");
	create("Out.Serviços", "622982", "020225");
    }

    private void create(final String description, final String economicClassification, final String pocCode) {
	new AcquisitionClassification(description, economicClassification, pocCode);
    }

}
