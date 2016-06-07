<?php

namespace AppBundle\Repository\Application;

use AppBundle\Entity\Application\ApiStats;
use Doctrine\ORM\EntityRepository;

/**
 * ApiStatsRepository Class
 */
class ApiStatsRepository extends EntityRepository
{
    /**
     * Find one by route name and date
     *
     * @param string    $routeName Route name
     * @param \DateTime $date      Date
     *
     * @return ApiStats|null ApiStats record
     */
    public function findOneByRouteNameAndDate($routeName, \DateTime $date)
    {
        $qb = $this->createQueryBuilder('s');

        return $qb->where($qb->expr()->eq('s.date', ':date'))
                  ->andWhere($qb->expr()->eq('s.routeName', ':route_name'))
                  ->setParameters([
                      'date'       => $date->format('Y-m-d'),
                      'route_name' => $routeName,
                  ])
                  ->getQuery()
                  ->getOneOrNullResult();
    }

    /**
     * Get stats by date
     *
     * @param \DateTime $date Date
     *
     * @return array API stats
     */
    public function getStatsByDate(\DateTime $date)
    {
        $qb = $this->createQueryBuilder('s');

        return $qb->select('s.routeName AS route')
                  ->addSelect('s.numberOfCalls AS number_of_calls')
                  ->where($qb->expr()->eq('s.date', ':date'))
                  ->setParameter('date', $date)
                  ->orderBy('number_of_calls', 'DESC')
                  ->getQuery()
                  ->getArrayResult();
    }
}