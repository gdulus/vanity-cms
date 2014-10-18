import org.apache.http.impl.client.DefaultHttpClient
import org.apache.http.impl.conn.PoolingClientConnectionManager
import vanity.utils.BootstrapUtils

beans = {

    xmlns context: "http://www.springframework.org/schema/context"
    xmlns task: "http://www.springframework.org/schema/task"

    context.'component-scan'('base-package': 'vanity.cms')
    task.'annotation-driven'('proxy-target-class': true, 'mode': 'proxy')

    httpClient(DefaultHttpClient) {
        PoolingClientConnectionManager manager = new PoolingClientConnectionManager()
        manager.maxTotal = 100
        it.constructorArgs = [manager]
    }

    bootstrapUtils(BootstrapUtils) {
        springSecurityService = ref('springSecurityService')
    }
}
