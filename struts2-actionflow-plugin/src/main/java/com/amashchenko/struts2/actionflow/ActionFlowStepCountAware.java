/*
 * Copyright 2013 Aleksandr Mashchenko.
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

/**
 * Actions that want to get hold of current action flow step count should
 * implement this interface.
 * 
 * @author Aleksandr Mashchenko
 * 
 */
public interface ActionFlowStepCountAware {
    /**
     * Sets the action flow step count in the implementing class.
     * 
     * @param stepCount
     *            action flow step count.
     */
    void setActionFlowStepCount(Integer stepCount);
}
