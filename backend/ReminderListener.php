<?php

namespace AppBundle\EventListener;

use AppBundle\Entity\Offer\OfferRequest;
use AppBundle\Event\AppEvents;
use AppBundle\Event\CandidateReminderEvent;
use AppBundle\Event\Notification\Reminder\ReminderForTomorrowForCandidateNotificationEvent;
use AppBundle\Event\Notification\Reminder\ReminderForTomorrowForRecruiterNotificationEvent;
use AppBundle\Event\RecruiterReminderEvent;
use AppBundle\Helper\DateHelper;
use Doctrine\ORM\EntityManager;
use Symfony\Component\EventDispatcher\EventDispatcherInterface;
use Symfony\Component\EventDispatcher\EventSubscriberInterface;
use Symfony\Component\Routing\Router;
use Symfony\Component\Translation\TranslatorInterface;

/**
 * ReminderListener
 */
class ReminderListener implements EventSubscriberInterface
{
    use NotificationTrait;

    /**
     * @var EntityManager $em Entity manager
     */
    private $em;

    /**
     * @var EventDispatcherInterface $dispatcher Dispatcher
     */
    private $dispatcher;

    /**
     * @var TranslatorInterface $translator Translator
     */
    private $translator;

    /**
     * @var Router $router Router
     */
    private $router;

    /**
     * Constructor
     *
     * @param EntityManager            $em         Entity manager
     * @param EventDispatcherInterface $dispatcher Dispatcher
     * @param TranslatorInterface      $translator Translator
     * @param Router                   $router     Router
     */
    public function __construct(
        EntityManager $em,
        EventDispatcherInterface $dispatcher,
        TranslatorInterface $translator,
        Router $router
    ) {
        $this->em         = $em;
        $this->dispatcher = $dispatcher;
        $this->translator = $translator;
        $this->router     = $router;
    }

    /**
     * {@inheritDoc}
     */
    public static function getSubscribedEvents()
    {
        return [
            AppEvents::REMINDER_FOR_TOMORROW_FOR_CANDIDATE => 'onReminderForTomorrowForCandidate',
            AppEvents::REMINDER_FOR_TOMORROW_FOR_RECRUITER => 'onReminderForTomorrowForRecruiter',
        ];
    }

    /**
     * On reminder for tomorrow for candidate
     *
     * @param CandidateReminderEvent $event Event
     */
    public function onReminderForTomorrowForCandidate(CandidateReminderEvent $event)
    {
        $candidate = $event->getCandidate();

        $offerRequestRepository = $this->em->getRepository('AppBundle:Offer\OfferRequest');
        $offerRequest = $offerRequestRepository->findHiredOfferRequestForCandidateForTomorrow($candidate);

        // Send notifications only if there is offer for tomorrow
        if ($offerRequest instanceof OfferRequest) {
            $event = new ReminderForTomorrowForCandidateNotificationEvent(
                $this->translator,
                $this->router,
                $event->getReminderDate(),
                $offerRequest
            );
            $this->prepareNotifications($event);
        }
    }

    /**
     * On reminder for tomorrow for recruiter
     *
     * @param RecruiterReminderEvent $event Event
     */
    public function onReminderForTomorrowForRecruiter(RecruiterReminderEvent $event)
    {
        $recruiter = $event->getRecruiter();

        $offerRequestRepository = $this->em->getRepository('AppBundle:Offer\OfferRequest');
        $numberOfHiredCandidates = $offerRequestRepository->getNumberOfHiredCandidatesForRecruiterForTomorrow($recruiter);

        // Send notification only if there are some hired candidates for tomorrow
        if ($numberOfHiredCandidates > 0) {
            $candidateRepository = $this->em->getRepository('AppBundle:Candidate\Candidate');

            $candidates = $candidateRepository->findHiredCandidatesOnSomeDateForRecruiter(
                $recruiter,
                DateHelper::getTomorrowDayForApplicationUser($recruiter)
            );

            $event = new ReminderForTomorrowForRecruiterNotificationEvent(
                $this->translator,
                $this->router,
                $event->getReminderDate(),
                $recruiter,
                $numberOfHiredCandidates,
                $candidates
            );
            $this->prepareNotifications($event);
        }
    }
}