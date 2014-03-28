/*
 * Copyright 2013-2014 Aleksandr Mashchenko.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.amashchenko.struts2.actionflow;

import javax.servlet.http.HttpSession;

import org.apache.struts2.StrutsJUnit4TestCase;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockPageContext;
import org.springframework.mock.web.MockServletContext;

import com.amashchenko.struts2.actionflow.mock.MockActionFlowAwareAction;
import com.amashchenko.struts2.actionflow.test.TestConstants;

/**
 * Tests ActionFlowAware feature.
 * 
 * @author Aleksandr Mashchenko
 * 
 */
public class ActionFlowAwareTest extends
        StrutsJUnit4TestCase<MockActionFlowAwareAction> {

    /** {@inheritDoc} */
    @Override
    protected String getConfigPath() {
        return "struts-plugin.xml, struts-test.xml";
    }

    /** Initializes servlet mock objects but preserves session. */
    private void initServletMockObjectsPreserveSession() {
        servletContext = new MockServletContext(resourceLoader);
        response = new MockHttpServletResponse();

        // preserve session
        HttpSession session = null;
        if (request != null && request.getSession() != null) {
            session = request.getSession();
        }
        request = new MockHttpServletRequest();
        request.setSession(session);

        pageContext = new MockPageContext(servletContext, request, response);
    }

    /**
     * Tests skipping action on 'next'.
     * 
     * @throws Exception
     *             when something goes wrong.
     */
    @Test
    public void testSkipNextAction() throws Exception {
        executeAction("/correctFlowAware/correctFlowAware");
        String previousAction = (String) findValueAfterExecute(TestConstants.SESSION_PREVIOUS_FLOW_ACTION);
        Assert.assertEquals(null, previousAction);
        String viewActionParam = (String) findValueAfterExecute(ActionFlowInterceptor.VIEW_ACTION_PARAM);
        Assert.assertEquals(null, viewActionParam);
        Integer highestCurrentIndex = (Integer) findValueAfterExecute(TestConstants.SESSION_HIGHEST_CURRENT_ACTION_INDEX);
        Assert.assertEquals(null, highestCurrentIndex);

        initServletMockObjectsPreserveSession();

        executeAction("/correctFlowAware/next");

        previousAction = (String) findValueAfterExecute(TestConstants.SESSION_PREVIOUS_FLOW_ACTION);
        Assert.assertEquals("savePhone-2", previousAction);

        viewActionParam = (String) findValueAfterExecute(ActionFlowInterceptor.VIEW_ACTION_PARAM);
        Assert.assertEquals("saveEmail-3View", viewActionParam);

        highestCurrentIndex = (Integer) findValueAfterExecute(TestConstants.SESSION_HIGHEST_CURRENT_ACTION_INDEX);
        Assert.assertEquals(new Integer(2), highestCurrentIndex);
    }

    /**
     * Tests skipping action on 'next' with not defined action name.
     * 
     * @throws Exception
     *             when something goes wrong.
     */
    @Test
    public void testSkipNextActionWrongActionName() throws Exception {
        executeAction("/correctFlowAware/correctFlowAware");
        String previousAction = (String) findValueAfterExecute(TestConstants.SESSION_PREVIOUS_FLOW_ACTION);
        Assert.assertEquals(null, previousAction);
        String viewActionParam = (String) findValueAfterExecute(ActionFlowInterceptor.VIEW_ACTION_PARAM);
        Assert.assertEquals(null, viewActionParam);
        Integer highestCurrentIndex = (Integer) findValueAfterExecute(TestConstants.SESSION_HIGHEST_CURRENT_ACTION_INDEX);
        Assert.assertEquals(null, highestCurrentIndex);

        initServletMockObjectsPreserveSession();

        request.setParameter("name",
                MockActionFlowAwareAction.WRONG_ACTION_NAME);
        executeAction("/correctFlowAware/next");

        previousAction = (String) findValueAfterExecute(TestConstants.SESSION_PREVIOUS_FLOW_ACTION);
        Assert.assertEquals("saveName-1", previousAction);

        viewActionParam = (String) findValueAfterExecute(ActionFlowInterceptor.VIEW_ACTION_PARAM);
        Assert.assertEquals("savePhone-2View", viewActionParam);

        highestCurrentIndex = (Integer) findValueAfterExecute(TestConstants.SESSION_HIGHEST_CURRENT_ACTION_INDEX);
        Assert.assertEquals(new Integer(1), highestCurrentIndex);
    }

    /**
     * Tests skipping action on 'next' with null value.
     * 
     * @throws Exception
     *             when something goes wrong.
     */
    @Test
    public void testSkipNextActionNull() throws Exception {
        executeAction("/correctFlowAware/correctFlowAware");
        String previousAction = (String) findValueAfterExecute(TestConstants.SESSION_PREVIOUS_FLOW_ACTION);
        Assert.assertEquals(null, previousAction);
        String viewActionParam = (String) findValueAfterExecute(ActionFlowInterceptor.VIEW_ACTION_PARAM);
        Assert.assertEquals(null, viewActionParam);
        Integer highestCurrentIndex = (Integer) findValueAfterExecute(TestConstants.SESSION_HIGHEST_CURRENT_ACTION_INDEX);
        Assert.assertEquals(null, highestCurrentIndex);

        initServletMockObjectsPreserveSession();

        request.setParameter("name", "null");
        executeAction("/correctFlowAware/next");

        previousAction = (String) findValueAfterExecute(TestConstants.SESSION_PREVIOUS_FLOW_ACTION);
        Assert.assertEquals("saveName-1", previousAction);

        viewActionParam = (String) findValueAfterExecute(ActionFlowInterceptor.VIEW_ACTION_PARAM);
        Assert.assertEquals("savePhone-2View", viewActionParam);

        highestCurrentIndex = (Integer) findValueAfterExecute(TestConstants.SESSION_HIGHEST_CURRENT_ACTION_INDEX);
        Assert.assertEquals(new Integer(1), highestCurrentIndex);
    }

    /**
     * Tests skipping action on 'prev'.
     * 
     * @throws Exception
     *             when something goes wrong.
     */
    @Test
    public void testSkipPrevAction() throws Exception {
        executeAction("/correctFlowAware/correctFlowAware");
        String previousAction = (String) findValueAfterExecute(TestConstants.SESSION_PREVIOUS_FLOW_ACTION);
        Assert.assertEquals(null, previousAction);
        String viewActionParam = (String) findValueAfterExecute(ActionFlowInterceptor.VIEW_ACTION_PARAM);
        Assert.assertEquals(null, viewActionParam);

        initServletMockObjects();

        // simulating 'prev' action
        request.getSession().setAttribute(
                TestConstants.IS_PREVIOUS_ACTION_PREV, true);
        executeAction("/correctFlowAware/savePhone-2View");

        previousAction = (String) findValueAfterExecute(TestConstants.SESSION_PREVIOUS_FLOW_ACTION);
        Assert.assertEquals("firstFlowAction", previousAction);

        viewActionParam = (String) findValueAfterExecute(ActionFlowInterceptor.VIEW_ACTION_PARAM);
        Assert.assertEquals("saveName-1View", viewActionParam);
    }

    /**
     * Tests skipping action on 'prev' with not defined action name.
     * 
     * @throws Exception
     *             when something goes wrong.
     */
    @Test
    public void testSkipPrevActionWrongActionName() throws Exception {
        executeAction("/correctFlowAware/correctFlowAware");
        String previousAction = (String) findValueAfterExecute(TestConstants.SESSION_PREVIOUS_FLOW_ACTION);
        Assert.assertEquals(null, previousAction);
        String viewActionParam = (String) findValueAfterExecute(ActionFlowInterceptor.VIEW_ACTION_PARAM);
        Assert.assertEquals(null, viewActionParam);

        initServletMockObjects();

        // simulating 'prev' action
        request.getSession().setAttribute(
                TestConstants.IS_PREVIOUS_ACTION_PREV, true);
        executeAction("/correctFlowAware/saveEmail-3View");

        previousAction = (String) findValueAfterExecute(TestConstants.SESSION_PREVIOUS_FLOW_ACTION);
        Assert.assertEquals(null, previousAction);

        viewActionParam = (String) findValueAfterExecute(ActionFlowInterceptor.VIEW_ACTION_PARAM);
        Assert.assertEquals(null, viewActionParam);
    }

    /**
     * Tests skipping action on 'prev' with null value.
     * 
     * @throws Exception
     *             when something goes wrong.
     */
    @Test
    public void testSkipPrevActionNull() throws Exception {
        executeAction("/correctFlowAware/correctFlowAware");
        String previousAction = (String) findValueAfterExecute(TestConstants.SESSION_PREVIOUS_FLOW_ACTION);
        Assert.assertEquals(null, previousAction);
        String viewActionParam = (String) findValueAfterExecute(ActionFlowInterceptor.VIEW_ACTION_PARAM);
        Assert.assertEquals(null, viewActionParam);

        initServletMockObjects();

        // simulating 'prev' action
        request.getSession().setAttribute(
                TestConstants.IS_PREVIOUS_ACTION_PREV, true);
        executeAction("/correctFlowAware/saveName-1View");

        previousAction = (String) findValueAfterExecute(TestConstants.SESSION_PREVIOUS_FLOW_ACTION);
        Assert.assertEquals(null, previousAction);

        viewActionParam = (String) findValueAfterExecute(ActionFlowInterceptor.VIEW_ACTION_PARAM);
        Assert.assertEquals(null, viewActionParam);
    }
}
