/*
 * Copyright 2016-2016 the original author or authors.
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

package com.github.persapiens.jsfboot.jetty;

import java.net.MalformedURLException;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;
import org.testng.annotations.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.jetty.JettyEmbeddedServletContainer;
import org.springframework.boot.context.embedded.jetty.JettyEmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.assertj.core.api.Assertions.assertThat;

@SpringApplicationConfiguration(classes = JettySpringBootAutoConfiguration.class)
@WebAppConfiguration
@Test
public class JettySpringBootAutoConfigurationIT extends AbstractTestNGSpringContextTests {

	@Autowired
	private JettySpringBootAutoConfiguration jettySpringBootAutoConfiguration;

	@Test
	public void customize() throws MalformedURLException {
		JettyEmbeddedServletContainerFactory factory = new JettyEmbeddedServletContainerFactory();

		this.jettySpringBootAutoConfiguration.customize(factory);

		Server server = ((JettyEmbeddedServletContainer) factory.getEmbeddedServletContainer()).getServer();

		Handler[] childHandlersByClass = server.getChildHandlersByClass(WebAppContext.class);
		WebAppContext webAppContext = (WebAppContext) childHandlersByClass[0];

		assertThat(webAppContext.getBaseResource().getResource("test.txt").exists())
			.isTrue();
	}

	@Test
	public void customizeTomcat() throws MalformedURLException {
		TomcatEmbeddedServletContainerFactory factory = new TomcatEmbeddedServletContainerFactory();

		this.jettySpringBootAutoConfiguration.customize(factory);

		assertThat(factory.getContextPath())
			.isEqualTo("");
	}
}
