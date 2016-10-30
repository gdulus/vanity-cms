package vanity.cms.celebrity

import groovy.util.logging.Slf4j
import org.apache.commons.lang.Validate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.transaction.annotation.Transactional
import org.springframework.transaction.support.TransactionSynchronizationAdapter
import org.springframework.transaction.support.TransactionSynchronizationManager
import vanity.celebrity.Celebrity
import vanity.celebrity.CelebrityImage
import vanity.celebrity.CelebrityImageStatus
import vanity.celebrity.Job
import vanity.cms.notification.EmailSender
import vanity.pagination.PaginationAware
import vanity.pagination.PaginationBean
import vanity.pagination.PaginationParams

@Slf4j
class CelebrityImageService implements PaginationAware<Job> {

    @Autowired
    EmailSender emailSender

    public CelebrityImage read(final Long id) {
        return CelebrityImage.read(id)
    }

    // TODO change sort order
    public PaginationBean<CelebrityImage> listWithPagination(final PaginationParams params) {
        if (params.queryParams.celebrityId) {
            Celebrity celebrity = Celebrity.load(params.queryParams.celebrityId)
            Integer count = CelebrityImage.countByCelebrityAndStateNotEqual(celebrity, CelebrityImageStatus.DELETED)
            List<CelebrityImage> result = CelebrityImage.findAllByCelebrityAndStateNotEqual(celebrity, CelebrityImageStatus.DELETED, [max: params.max, offset: params.offset, sort: params.sort])
            return new PaginationBean<>(result, count)
        } else {
            Integer count = CelebrityImage.countByStateNotEqual(CelebrityImageStatus.DELETED)
            List<CelebrityImage> result = CelebrityImage.findAllByStateNotEqual(CelebrityImageStatus.DELETED, [max: params.max, offset: params.offset, sort: params.sort])
            return new PaginationBean<>(result, count)
        }
    }

    @Transactional
    public CelebrityImage approve(final Long id) {
        CelebrityImage image = CelebrityImage.get(id)
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
            @Override
            public void afterCommit() {
                try {
                    emailSender.sendImageApproved(image)
                } catch (Exception exp) {
                    log.error("There was an error while sending an email seapproving celebrity image with id = ${id} ", exp)
                }
            }
        })
        return changeStatus(image, CelebrityImageStatus.REVIEWED)
    }

    @Transactional
    public CelebrityImage delete(final Long id) {
        return changeStatus(CelebrityImage.get(id), CelebrityImageStatus.DELETED)
    }

    private CelebrityImage changeStatus(final CelebrityImage image, final Integer status) {
        Validate.notNull(image)
        Validate.isTrue(CelebrityImageStatus.isSupported(status))
        image.state = status
        image.save()
        return image
    }

}
