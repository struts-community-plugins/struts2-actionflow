<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page contentType="text/html; charset=UTF-8"%>

<h4>simple example</h4>
<s:include value="steps.jsp"/>

<s:form action="next">
	<s:hidden name="step" value="%{#session['actionFlowPreviousAction']}"/>

	<s:textfield key="phone" label="Phone" />

	<tr><td><br/></td></tr>

	<s:include value="submit-buttons.jsp"/>
</s:form>
		
<br/>
<div class="example-code">		
    <i>Form:</i>
<pre>
&lt;s:form action="next"&gt;
    &lt;s:hidden name="step" value="%{#session['actionFlowPreviousAction']}" /&gt;

    &lt;s:textfield key="phone" label="Phone" /&gt;

    &lt;s:submit value="previous" action="prev" /&gt;
    &lt;s:submit value="next" action="next" /&gt;
&lt;/s:form&gt;
</pre>
        
    <i>Action configuration:</i>
<pre>
&lt;action name="savePhone" method="savePhone" class="..."&gt;
    &lt;param name="actionFlowStep"&gt;2&lt;/param&gt;
			
    &lt;result name="input"&gt;/WEB-INF/simple/phone.jsp&lt;/result&gt;
    &lt;result name="error"&gt;/WEB-INF/simple/phone.jsp&lt;/result&gt;
    &lt;result type="redirectAction"&gt;finish&lt;/result&gt;
&lt;/action&gt;
</pre>
</div>
