/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 * 
 * Copyright @2010 bemoko 
 */

/*
 * Test implementation of wordpress XML RPC API against reference sample xml responses
 *
 * groovy wordpress/tests/WordpressTest.groovy 
 */ 
package wordpress.tests

import groovy.util.GroovyTestCase
import org.apache.xmlrpc.common.XmlRpcRequestProcessor
import wordpress.Wordpress
import wordpress.domain.Post

@Grab(group='org.apache.xmlrpc', module='xmlrpc-client', version='3.1.2')
class WordpressTest extends GroovyTestCase  {
    
    def testUrlBase="file:///"+new File(getClass().protectionDomain.codeSource.location.path).parent
    
    def newBlogConnection(String file) {
        def blogSource=new Wordpress()
        blogSource.initialise(['blogUrl':testUrlBase+"/${file}",'blogId':'1','username':'xxx','password':'xxx'])
        blogSource.client.setTransportFactory(new XmlRpcStubServer(blogSource.client))
        return blogSource
    }
    
    void testRecentPosts() {
        def blog=newBlogConnection('RecentPosts.xml')
        List posts=blog.getRecentPosts(3)
        assertEquals("Recent post titles not 3",3,posts.size)
        def post1=posts[0]
        
        assertEquals("post id is incorrect","825",post1.postId)
        assertEquals("post link is incorrect","http://blog.bemoko.com/?p=825",post1.link)
        assertEquals("post permalink is incorrect","http://blog.bemoko.com/?p=825",post1.permaLink)
        assertEquals("post status is incorrect","draft",post1.postStatus)
        assertEquals("post title is incorrect","Test post 1",post1.title)
        assertEquals("post description is incorrect","Test description 1",post1.description)
        assertEquals("post author id is incorrect","3",post1.authorId)
        assertEquals("post author name is incorrect","Tim Avery",post1.authorName)
        assertEquals("post date created is incorrect","Fri Jan 15 16:43:01 GMT 2010",post1.dateCreated.toString())
        
        assertEquals("post tags size is incorrect",0,post1.tagList.size())
        assertEquals("post categories size is incorrect",1,post1.categories.size())
        assertEquals("post first category is incorrect","mobile",post1.categories[0])
    }
    
    void testRecentPostTitles() {
        def blog=newBlogConnection('RecentPostTitles.xml')
        List posts=blog.getRecentPostTitles(3)
        assertEquals("Recent posts not 3",3,posts.size)
        def post1=posts[0]
        
        assertEquals("post title is incorrect",'Test post 1',post1.title)
        assertEquals("post user id is incorrect",'3',post1.authorId)
        assertEquals("post id is incorrect",'825',post1.postId)
        assertEquals("post date created is incorrect","Fri Jan 15 16:43:01 GMT 2010",post1.dateCreated.toString())
    }
    
    void testGetPost() {
        def blog=newBlogConnection('GetPost.xml')
        Post post=blog.getPost(671)
        assertNotNull("post is null",post)
        assertEquals("post id is incorrect","671",post.postId)
        assertEquals("post link is incorrect","http://blog.bemoko.com/2009/10/29/tribeca-film-festival/",post.link)
        assertEquals("post permalink is incorrect","http://blog.bemoko.com/2009/10/29/tribeca-film-festival/",post.permaLink)
        assertEquals("post status is incorrect","publish",post.postStatus)
        assertEquals("post title is incorrect","Test post title",post.title)
        assertEquals("post description is incorrect","Test post description",post.description)
        assertEquals("post author id is incorrect","4",post.authorId)
        assertEquals("post author name is incorrect","Ian Walsh",post.authorName)
        assertEquals("post date created is incorrect","Thu Oct 29 18:46:28 GMT 2009",post.dateCreated.toString())
        
        assertEquals("post tags size is incorrect",3,post.tagList.size())
        assertEquals("post first tag is incorrect","customers",post.tagList[0])
        assertEquals("post categories size is incorrect",2,post.categories.size())
        assertEquals("post first category is incorrect","mobile web",post.categories[0])
    }    
}