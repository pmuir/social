package org.jboss.seam.social.test;

import java.io.File;
import java.io.FileNotFoundException;

import javax.inject.Inject;

import org.jboss.arquillian.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.seam.social.oauth.HasStatus;
import org.jboss.seam.social.oauth.HttpResponse;
import org.jboss.seam.social.oauth.JsonMapper;
import org.jboss.seam.social.oauth.OAuthService;
import org.jboss.seam.social.oauth.OAuthServiceScribe;
import org.jboss.seam.social.oauth.OAuthServiceSettings;
import org.jboss.seam.social.oauth.OAuthServiceSettingsImpl;
import org.jboss.seam.social.oauth.OAuthSessionSettings;
import org.jboss.seam.social.oauth.OAuthSessionSettingsImpl;
import org.jboss.seam.social.oauth.OAuthToken;
import org.jboss.seam.social.oauth.OAuthTokenScribe;
import org.jboss.seam.social.oauth.RelatedTo;
import org.jboss.seam.social.oauth.RestVerb;
import org.jboss.seam.social.oauth.UserProfile;
import org.jboss.seam.social.twitter.Twitter;
import org.jboss.seam.social.twitter.TwitterScribe;
import org.jboss.seam.social.twitter.model.CredentialJackson;
import org.jboss.seam.social.twitter.model.Tweet;
import org.jboss.seam.social.twitter.model.TweetJackson;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.GenericArchive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.FileAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.DependencyResolvers;
import org.jboss.shrinkwrap.resolver.api.maven.MavenDependencyResolver;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class TwitterTest {
    @Inject
    Twitter twitter;

    @Deployment
    public static Archive<?> createTestArchive() throws FileNotFoundException {
        File beanFile = new File("src/test/resources/META-INF/beans.xml");
        if (!beanFile.exists())
            throw new FileNotFoundException();
        Archive<?> ret = ShrinkWrap
                .create(WebArchive.class, "test.war")
                .addClasses(
                    TwitterScribe.class, Twitter.class, OAuthService.class, OAuthServiceScribe.class,
                    OAuthServiceSettings.class, OAuthServiceSettingsImpl.class, OAuthSessionSettings.class,
                    OAuthSessionSettingsImpl.class, RelatedTo.class,
                    JsonMapper.class, HasStatus.class, 
                    OAuthToken.class, RestVerb.class, HttpResponse.class, UserProfile.class,
                    Tweet.class, TweetJackson.class, CredentialJackson.class,
                    OAuthTokenScribe.class)

                .addAsLibraries(DependencyResolvers.use(MavenDependencyResolver.class)
                        .loadReposFromPom("pom.xml")
                        .artifact("org.jboss.seam.config:seam-config-xml")
                        .artifact("org.jboss.seam.solder:seam-solder")
                        .artifact("org.scribe:scribe")
                		.resolveAs(GenericArchive.class))
                .addAsWebInfResource(new FileAsset(beanFile), ArchivePaths.create("classes/META-INF/beans.xml"));
        
        // ret.as(ZipExporter.class).exportTo(new File("mytest.war"), true);
        
        return ret;
    }
    
    @Test
    public void authorizationUrlTest()
    {
        Assert.assertTrue(twitter.getAuthorizationUrl().startsWith("http"));
    }
}
