<?php

namespace AppBundle\Security\Authorization\Voter;

use AppBundle\Entity\Application\ApplicationUser;
use AppBundle\Entity\Offer\Offer;
use AppBundle\Entity\Recruiter\Recruiter;
use Symfony\Component\Security\Core\Authentication\Token\TokenInterface;
use Symfony\Component\Security\Core\Authorization\Voter\Voter;

/**
 * OfferVoter
 */
class OfferVoter extends Voter
{
    const IS_OWNER = 'IS_OWNER';
    const IS_ALLOWED_TO_UNHIRE = 'IS_ALLOWED_TO_UNHIRE';

    /**
     * @var array $attributes Attributes
     */
    private $attributes = [
        self::IS_OWNER,
        self::IS_ALLOWED_TO_UNHIRE,
    ];

    /**
     * {@inheritdoc}
     */
    protected function supports($attribute, $subject)
    {
        $result = false;

        if (in_array($attribute, $this->attributes) && $subject instanceof Offer) {
            $result = true;
        }

        return $result;
    }

    /**
     * Vote on attribute
     *
     * @param string         $attribute Attribute
     * @param Offer          $offer     Offer
     * @param TokenInterface $token     Token
     *
     * @return bool
     */
    protected function voteOnAttribute($attribute, $offer, TokenInterface $token)
    {
        $user = $token->getUser();

        $result = false;

        switch ($attribute) {
            case self::IS_OWNER:
                // Check if current user is the owner of the offer
                if ($user instanceof ApplicationUser && $user->getRecruiter() instanceof Recruiter) {
                    if ($offer->getRecruiter()->getId() === $user->getRecruiter()->getId()) {
                        $result = true;
                    }
                }
                break;
            case self::IS_ALLOWED_TO_UNHIRE:
                if ($offer->isPublished()) {
                    $result = true;
                }
                break;
            default:
                break;
        }

        return $result;
    }
}
