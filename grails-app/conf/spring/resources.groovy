beans = {

    xmlns context: "http://www.springframework.org/schema/context"
    xmlns task:"http://www.springframework.org/schema/task"

    context.'component-scan'('base-package': 'vanity.cms')
    task.'annotation-driven'('proxy-target-class':true, 'mode':'proxy')
}
