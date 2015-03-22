package vanity.cms.celebrity

import grails.plugin.springsecurity.annotation.Secured
import org.springframework.beans.factory.annotation.Value
import vanity.celebrity.CelebrityJobService
import vanity.celebrity.Job
import vanity.pagination.PaginationParams
import vanity.user.Authority

@Secured([Authority.ROLE_ADMIN])
class CelebrityJobController {

    CelebrityJobService celebrityJobService

    @Value('${cms.celebrity.jobs.pagination.max}')
    Long defaultMaxJobs

    def index(final Long offset, final Long max) {
        Long maxValue = max ?: defaultMaxJobs
        PaginationParams paginationParams = new PaginationParams(maxValue, offset, 'name')
        [paginationBean: celebrityJobService.listWithPagination(paginationParams)]
    }

    def create() {}

    def save(final CelebrityJobCmd cmd) {
        if (!cmd.validate()) {
            flash.error = 'vanity.cms.celebrity.job.savingDomainError'
            return render(view: 'create', model: [celebrityJob: cmd])
        }

        Job celebrityJob = celebrityJobService.save(cmd.name)

        if (!celebrityJob.hasErrors()) {
            flash.info = 'vanity.cms.celebrity.job.saved'
            return redirect(action: 'edit', id: celebrityJob.id)
        } else {
            flash.error = 'vanity.cms.celebrity.job.savingDomainError'
            return render(view: 'create', model: [celebrityJob: celebrityJob])
        }
    }

    def edit(final Long id) {
        [celebrityJob: celebrityJobService.read(id)]
    }

    def update(final CelebrityJobCmd cmd) {
        if (!cmd.validate()) {
            flash.error = 'vanity.cms.celebrity.job.savingDomainError'
            return render(view: 'edit', model: [celebrityJob: cmd])
        }

        Job celebrityJob = celebrityJobService.update(cmd.id, cmd.name)

        if (!celebrityJob) {
            flash.error = 'vanity.cms.entity.notFound'
            return redirect(action: 'index')
        }

        if (celebrityJob.hasErrors()) {
            flash.error = 'vanity.cms.celebrity.job.savingDomainError'
            return render(view: 'edit', model: [celebirtyJob: celebrityJob])
        } else {
            flash.info = 'vanity.cms.celebrity.job.saved'
            return redirect(action: 'edit', id: celebrityJob.id)
        }
    }

    def delete(final Long id) {
        celebrityJobService.delete(id)
        flash.info = 'vanity.cms.celebrity.job.deleted'
        redirect(action: 'index')
    }

}
