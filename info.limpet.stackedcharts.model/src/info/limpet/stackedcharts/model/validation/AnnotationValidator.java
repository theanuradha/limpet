/**
 *
 * $Id$
 */
package info.limpet.stackedcharts.model.validation;

import info.limpet.stackedcharts.model.Chart;

/**
 * A sample validator interface for {@link info.limpet.stackedcharts.model.Annotation}.
 * This doesn't really do anything, and it's not a real EMF artifact.
 * It was generated by the org.eclipse.emf.examples.generator.validator plug-in to illustrate how EMF's code generator can be extended.
 * This can be disabled with -vmargs -Dorg.eclipse.emf.examples.generator.validator=false.
 */
public interface AnnotationValidator
{
  boolean validate();

  boolean validateName(String value);
  boolean validateColor(String value);
  boolean validateChart(Chart value);
}
