package bench

trait EnclosingObjectMarkerTrait {
  def whatIwant: String
}

trait InnerMarkerTrait

object EnclosingObject extends EnclosingObjectMarkerTrait {

  // This is what we're looking for here:
  override val whatIwant: String = "This is what i'm looking for"

  class AnInnerClass extends InnerMarkerTrait {
  }

}