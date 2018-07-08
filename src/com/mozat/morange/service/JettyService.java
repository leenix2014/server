package com.mozat.morange.service;

import javax.servlet.MultipartConfigElement;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mozat.morange.service.servlet.AgentHttpServlet;
import com.mozat.morange.service.servlet.AnchorHttpServlet;
import com.mozat.morange.service.servlet.ExistsAnchorHttpServlet;
import com.mozat.morange.service.servlet.RefreshHttpServlet;
import com.mozat.morange.service.servlet.UpdAnchorMacHttpServlet;

public class JettyService {
    private static Logger log = LoggerFactory.getLogger(JettyService.class);
    private int port = 8087;
    private Server jetty;

    //protected JettyService() {
    public JettyService() {
        try {
            CompositeConfiguration settings = new CompositeConfiguration();
            settings.addConfiguration(new PropertiesConfiguration("config_deploy/system.properties"));
            Configuration serverConf = settings.subset("service").subset("jetty");
            port = serverConf.getInt("port");
        } catch (Exception e) {
            log.error("load system setting error: ", e);
            System.exit(1);
        }
    }

    //protected void start() throws Exception {
    public void start() throws Exception {
        init();
        jetty.start();
    }

    protected void stop() {
        try {
            jetty.stop();
        } catch (Exception e) {
            log.error("stop jetty failed:", e);
        }
    }

    private void init() {
        jetty = new Server();

        Connector connector = new SelectChannelConnector();
        connector.setPort(port);
        //connector.setPort(8088);
        connector.setMaxIdleTime(120000);
        connector.setLowResourceMaxIdleTime(30000);
        jetty.addConnector(connector);

        QueuedThreadPool threadPool = new QueuedThreadPool();
        threadPool.setMinThreads(20);
        threadPool.setMaxThreads(100);
        jetty.setThreadPool(threadPool);

        ServletContextHandler servlet = new ServletContextHandler(ServletContextHandler.SESSIONS);
        servlet.addServlet(new ServletHolder(new AgentHttpServlet()), "/agent");
        servlet.addServlet(new ServletHolder(new AnchorHttpServlet()), "/anchor");
        servlet.addServlet(new ServletHolder(new RefreshHttpServlet()), "/refresh");
        
        ServletHolder existsAnchor = new ServletHolder(new ExistsAnchorHttpServlet());
        existsAnchor.getRegistration().setMultipartConfig(new MultipartConfigElement("data/tmp"));
        servlet.addServlet(existsAnchor, "/existsAnchor");
        
        ServletHolder updateMachine = new ServletHolder(new UpdAnchorMacHttpServlet());
        updateMachine.getRegistration().setMultipartConfig(new MultipartConfigElement("data/tmp"));
        servlet.addServlet(updateMachine, "/updateMachine");
        
        HandlerList list = new HandlerList();
        list.addHandler(servlet);
        jetty.setHandler(list);
    }

}
