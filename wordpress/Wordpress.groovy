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
 * Plugin to access the wordpress XML RPC API methods
 * For wordpress feed API consider using the ATOM interface rather then XML RPC
 * 
 */

package wordpress

import com.bemoko.live.platform.mwc.plugins.AbstractPlugin
import org.apache.xmlrpc.client.XmlRpcClient
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl

import wordpress.domain.Post

@Grab(group='org.apache.xmlrpc', module='xmlrpc-client', version='3.1.2')
class Wordpress extends AbstractPlugin {
    
    private XmlRpcClient client
    protected def blogId
    protected def username
    protected def password
    
    void initialise(Map map) {
        initialiseXmlRpcClient(map.blogUrl)
        blogId=map.blogId
        username=map.username
        password=map.password
    }
    
    protected void initialiseXmlRpcClient(String url) {
        def config = new XmlRpcClientConfigImpl()
        config.setServerURL(new URL(url))
        client = new XmlRpcClient()
        client.setConfig(config)
    }
    
    /**
     * Implementation of the MetaWeblog metaWeblog.getRecentPosts API
     */
    List getRecentPosts(int count){
        Object[] params = [blogId,username,password,count]
        List posts=[] 
        client.execute("metaWeblog.getRecentPosts",params).each{it->		
            posts+=new Post(
            postId:it.postid,
            permaLink:it.permaLink,
            postStatus:it.post_status,
            link:it.link,
            title:it.title,
            authorId:it.userid,
            authorName:it.wp_author_display_name,
            dateCreated:it.dateCreated,
            description:it.description,
            categories: it.categories
            )
        }
        return posts
    }
    
    /**
     * implementation of MoveableType mt.getRecentPostTitles
     */
    List getRecentPostTitles(int count){
        Object[] params = [blogId,username,password,count]
        List posts=[] 
        client.execute("mt.getRecentPostTitles",params).each{it->		
            posts+=new Post(
            postId:it.postid,
            title:it.title,
            authorId:it.userid,
            dateCreated:it.dateCreated
            )
        }
        return posts
    }
    
    /**
     * implementation of MetaWeblog metaWeblog.getPost
     */
    Post getPost(def Id) {
        Object[] params = [Id,username,password]
        
        def postReturn=client.execute("metaWeblog.getPost",params)
        def post=new Post(
                postId:postReturn.postid,
                permaLink:postReturn.permaLink,
                postStatus:postReturn.post_status,
                link:postReturn.link,
                title:postReturn.title,
                authorId:postReturn.userid,
                authorName:postReturn.wp_author_display_name,
                dateCreated:postReturn.dateCreated,
                description:postReturn.description,
                categories: postReturn.categories,
                tags:postReturn.mt_keywords
                )
        return post
    }   
}