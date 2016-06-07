<?php

namespace AppBundle\Form\DataTransformer;

use AppBundle\Entity\Application\ApplicationUser;
use AppBundle\Security\PasswordGenerator;
use Doctrine\ORM\EntityManager;
use Symfony\Component\Form\DataTransformerInterface;
use Symfony\Component\Form\Exception\TransformationFailedException;

/**
 * ApplicationUserToPhoneNumberIdTransformer
 */
class ApplicationUserToPhoneNumberIdTransformer implements DataTransformerInterface
{
    /**
     * @var EntityManager $em Entity manager
     */
    private $em;

    /**
     * Constructor
     *
     * @param EntityManager $manager Entity manager
     */
    public function __construct(EntityManager $manager)
    {
        $this->em = $manager;
    }

    /**
     * Transforms an object (application user) to an integer (phone number ID)
     *
     * @param ApplicationUser|null $applicationUser Application user
     *
     * @return string
     */
    public function transform($applicationUser)
    {
        if (null === $applicationUser) {
            return null;
        }

        return $applicationUser->getPhoneNumber()->getId();
    }

    /**
     * Transforms a phone number ID (integer) to an object (application user)
     *
     * @param string $phoneNumberId Phone number ID
     *
     * @return ApplicationUser|null Application user
     */
    public function reverseTransform($phoneNumberId)
    {
        $phoneNumber = null;

        if (!$phoneNumberId) {
            return $phoneNumber;
        }

        $phoneNumber = $this->em->getRepository('AppBundle:Application\PhoneNumber')->findOneById($phoneNumberId);

        if (null === $phoneNumber) {
            throw new TransformationFailedException(sprintf('A phone number with ID "%s" does not exist!', $phoneNumberId));
        }

        $applicationUser = $this->em->getRepository('AppBundle:Application\ApplicationUser')->findOneByPhoneNumber($phoneNumber);

        if (null === $applicationUser) {
            $applicationUser = (new ApplicationUser())->setPhoneNumber($phoneNumber);
        }
        if ($applicationUser instanceof ApplicationUser && (!$applicationUser->isRecruiter() || !$applicationUser->isCandidate())) {
            $password = PasswordGenerator::generateRandomPassword();
            $applicationUser->setPlainPassword($password);
            $applicationUser->setPassword($password);
        }

        return $applicationUser;
    }
}