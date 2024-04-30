package com.khanr1
package cryocompose
package wiring

import cats.Show

/** An enumeration representing standard cable lengths in the context of a  dilution fridge.
  */
enum StageLength:
  case RT_50K
  case `50K_4K`
  case `4K_Still`
  case Still_CP
  case CP_MXC

object StageLength:
  given show: Show[StageLength] = Show.show(f =
    x =>
      x match
        case RT_50K => "from room temperature to 50K flange"
        case `50K_4K` => "from 50K flange to 4K flange"
        case `4K_Still` => "from 4K flange to Still flange"
        case Still_CP => "from Still flange to Cold Plate"
        case CP_MXC => "from Cold Plate to Mixing Chamber flange"
  )
