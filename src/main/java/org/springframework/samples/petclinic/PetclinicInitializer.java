/*
 * Copyright 2002-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.petclinic;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.XmlWebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.support.AbstractDispatcherServletInitializer;
    @Override
 * {@link ServletContext} programmatically.
 * <p/>
 * Create the Spring "<strong>root</strong>" application context.<br/>
 * Register a {@link DispatcherServlet}  in the servlet context.<br/>
 * For both servlets, register a {@link CharacterEncodingFilter}.
 * <p/>
 *
 * @author Antoine Rey
 */
public class PetclinicInitializer extends AbstractDispatcherServletInitializer {

    @Override
     * <p>
     * You also may use the -Dspring.profiles.active=jdbc VM options to change
     * default jpa Spring profile.
     */
    }
        return rootAppContext;
    }

    @Override
    protected Filter[] getServletFilters() {
        // Used to provide the ability to enter Chinese characters inside the Owner Form
        CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter("UTF-8", true);
        return new Filter[]{characterEncodingFilter};
    }

}
