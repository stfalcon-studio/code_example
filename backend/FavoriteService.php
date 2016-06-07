<?php

namespace AppBundle\Service;

use AppBundle\Entity\Candidate\Candidate;
use AppBundle\Entity\Candidate\FavoriteRecruiterOfCandidate;
use AppBundle\Entity\Recruiter\FavoriteCandidateOfRecruiter;
use AppBundle\Entity\Recruiter\Recruiter;
use Doctrine\ORM\EntityManager;

/**
 * FavoriteService
 */
class FavoriteService
{
    /**
     * @var EntityManager $em Entity manager
     */
    private $em;

    /**
     * Constructor
     *
     * @param EntityManager $em Entity manager
     */
    public function __construct(EntityManager $em)
    {
        $this->em = $em;
    }

    /**
     * Is recruiter favorited by candidate?
     *
     * @param Recruiter $recruiter Recruiter
     * @param Candidate $candidate Candidate
     *
     * @return bool
     */
    public function isRecruiterFavoritedByCandidate(Recruiter $recruiter, Candidate $candidate)
    {
        $favoriteRepository = $this->em->getRepository('AppBundle:Candidate\FavoriteRecruiterOfCandidate');
        $favorite = $favoriteRepository->findOneByCandidateAndRecruiter($candidate, $recruiter);

        return $favorite instanceof FavoriteRecruiterOfCandidate && $favorite->isFavorited();
    }

    /**
     * Is candidate favorited by recruiter?
     *
     * @param Candidate $candidate Candidate
     * @param Recruiter $recruiter Recruiter
     *
     * @return bool
     */
    public function isCandidateFavoritedByRecruiter(Candidate $candidate, Recruiter $recruiter)
    {
        $favoriteRepository = $this->em->getRepository('AppBundle:Recruiter\FavoriteCandidateOfRecruiter');
        $favorite = $favoriteRepository->findOneByRecruiterAndCandidate($recruiter, $candidate);

        return $favorite instanceof FavoriteCandidateOfRecruiter && $favorite->isFavorited();
    }
}