FROM sonarqube:lts
ENV SONARQUBE_HOME /opt/sonarqube
RUN rm extensions/plugins/sonar-html-plugin-3.1.0.1615.jar
COPY sonar-html-plugin/target/*.jar $SONARQUBE_HOME/extensions/plugins/
EXPOSE 9000