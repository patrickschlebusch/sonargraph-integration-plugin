/*******************************************************************************
 * Jenkins Sonargraph Integration Plugin
 * Copyright (C) 2015-2016 hello2morrow GmbH
 * mailto: info AT hello2morrow DOT com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions
 * and limitations under the License.
 *******************************************************************************/
package com.hello2morrow.sonargraph.integration.jenkins.controller;

import hudson.FilePath;
import hudson.model.Action;
import hudson.model.DirectoryBrowserSupport;

import java.io.IOException;
import java.util.logging.Level;

import javax.servlet.ServletException;

import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;

import com.hello2morrow.sonargraph.integration.jenkins.foundation.SonargraphLogger;
import com.hello2morrow.sonargraph.integration.jenkins.persistence.TextFileReader;

public abstract class AbstractHTMLAction implements Action
{

    protected void enableDirectoryBrowserSupport(StaplerRequest req, StaplerResponse rsp, FilePath directoryToServe)
            throws IOException, ServletException
    {
        DirectoryBrowserSupport directoryBrowser = new DirectoryBrowserSupport(this, directoryToServe, this.getDisplayName() + "html2", "graph.gif",
                false);
        directoryBrowser.generateResponse(req, rsp, this);
    }

    protected String readHTMLReport(FilePath pathToReport) throws IOException, InterruptedException
    {
        SonargraphLogger.INSTANCE.log(Level.INFO, "Reading Sonargraph HTML Report from '" + pathToReport + "'");
        TextFileReader fileReader = new TextFileReader();
        String htmlReport;

        if (pathToReport.exists())
        {
            htmlReport = fileReader.readLargeTextFile(pathToReport);
        }
        else
        {
            SonargraphLogger.INSTANCE.log(Level.INFO, "Unable to read Sonargraph HTML report from '" + pathToReport + "'");
            htmlReport = "Unable to read Sonargraph HTML report.";
        }

        return htmlReport;
    }

    public abstract void doDynamic(StaplerRequest req, StaplerResponse rsp) throws IOException, ServletException;

    public abstract String getHTMLReport() throws IOException, InterruptedException;
}
