<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

No caso particular das despesas com deslocações em viaturas próprias ou
alugadas, a sua apresentação está condicionada a autorização prévia por parte
do Conselho Directivo nesse sentido.

3. O pedido de autorização de utilização de viatura própria tem que ser sempre
   formulado antes de ser efectuada a deslocação (consultar formulário em anexo
   4.9).

4. No que respeita a projectos, é possível solicitar uma autorização genérica para
   todas as deslocações no âmbito do projecto e durante a vigência do mesmo, desde
   que devidamente justificada a necessidade de utilização de viatura própria. O
   pedido de autorização terá que indicar ainda os locais de destino das deslocações
   bem como, uma previsão do número de viagens a efectuar durante o período em
   causa.

      Aquando do pedido de autorização para utilização de viatura própria, pode o
      interessado optar pela utilização da mesma por interesse do serviço ou por
      conveniência do próprio. Neste último caso o funcionário apenas será
      ressarcido do montante equivalente ao custo das passagens do transporte
      colectivo.
5. As deslocações ao estrangeiro em viatura própria carecem de justificação e de
   autorização prévia por parte do Conselho Directivo do IST. A solicitação em causa
   deve ser acompanhada de um orçamento de uma Agência de Viagens com o valor
   do bilhete de avião para o destino pretendido em tarifa PEX/Económica.


<div class="infobox">
	<fr:view name="missionItem" schema="module.mission.domain.PersonalVehiclItem.view">
						<fr:layout name="tabular">
							<fr:property name="classes" value="structural thmiddle thlight mvert05"/>
						</fr:layout>
	</fr:view>
</div>

