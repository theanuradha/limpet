/**
 */
package info.limpet.stackedcharts.model.tests;

import info.limpet.stackedcharts.model.Marker;
import info.limpet.stackedcharts.model.StackedchartsFactory;

import junit.textui.TestRunner;

/**
 * <!-- begin-user-doc -->
 * A test case for the model object '<em><b>Marker</b></em>'.
 * <!-- end-user-doc -->
 * @generated
 */
public class MarkerTest extends AbstractAnnotationTest
{

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public static void main(String[] args)
  {
    TestRunner.run(MarkerTest.class);
  }

  /**
   * Constructs a new Marker test case with the given name.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public MarkerTest(String name)
  {
    super(name);
  }

  /**
   * Returns the fixture for this Marker test case.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  protected Marker getFixture()
  {
    return (Marker)fixture;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see junit.framework.TestCase#setUp()
   * @generated
   */
  @Override
  protected void setUp() throws Exception
  {
    setFixture(StackedchartsFactory.eINSTANCE.createMarker());
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see junit.framework.TestCase#tearDown()
   * @generated
   */
  @Override
  protected void tearDown() throws Exception
  {
    setFixture(null);
  }

} //MarkerTest
