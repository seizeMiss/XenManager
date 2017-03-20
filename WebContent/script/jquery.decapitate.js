/**
 * jQuery.decapitate
 * @author Clay McClure <clay@daemons.net>
 * @requires jQuery 1.7 & Bootstrap 2.x Affix plug-in
 *
 * Copyright (c) 2013, Clay McClure
 * All rights reserved.
 *
 * Licensed under the New BSD License
 * See: http://www.opensource.org/licenses/bsd-license.php
 */
(function($) {
    var Decapitate = function(element, options) {
        this.options = $.extend({}, $.fn.decapitate.defaults, options);
        this.$element = $(element);

        var $bodyTable = this.$element,
            $headTable = $('<table></table>'),
            $bodyWrapper = $('<div></div>'),
            $headWrapper = $('<div></div>'),
            $headCells = $bodyTable.find('thead tr').last().children(),
            $bodyCells = $bodyTable.find('tbody tr').first().children(),
            $lastRow = $bodyTable.find('tr').last(),
            tableWidth = $bodyTable.width();

        // Specify table column widths as percentages
        // NB: This fails if $headCells or $bodyCells contains a cell with colspan > 1
        $headCells.each(function(i, cell) {
            var $cell = $(cell),
                cellWidth = $cell.width(),
                percent = (100.0 * cellWidth / tableWidth).toString() + '%';

            $cell.attr('width', percent);
            $($bodyCells[i]).attr('width', percent);
        });

        // Clone <caption> and <thead> to decap-head table
        $headTable
            .append($bodyTable.find('caption').clone())
            .append($bodyTable.find('thead').clone());
        
        // Copy CSS classes from decap-body table to decap-head table
        $headTable.attr('class', $bodyTable.attr('class'));

        // Add decap-* styles so each table can be styled correctly
        $bodyWrapper.addClass('decap-body-wrapper');
        $headWrapper.addClass('decap-head-wrapper');
        $headTable.addClass('decap-head');
        $bodyTable.addClass('decap-body');

        // Give the bodyWrapper an id
        $bodyWrapper.attr('id', $bodyTable.attr('id') + '-wrapper');

        // Insert the headWrapper <div> and move the decap-head table into it
        $headWrapper
            .insertBefore($bodyTable)
            .append($headTable);

        // Insert the bodyWrapper <div> and move both tables into it
        $bodyWrapper
            .insertBefore($bodyTable)
            .append($headWrapper)
            .append($bodyTable);

        $headWrapper.affix({
            offset: {
                top: function() { return Math.ceil($bodyWrapper.offset().top) },
                bottom: function() { return Math.ceil($(document).height() - $lastRow.offset().top) }
            }
        });

        // Resizing the window may push content down, so we need to recheck the table's position
        $(window).resize(function() {
           $headWrapper.affix('checkPosition');
        });
    };

    var old = $.fn.decapitate;
  
    $.fn.decapitate = function(option) {
        return this.each(function() {
            var $this = $(this),
                data = $this.data('decapitate'),
                options = typeof option === 'object' && option;
            
            if (!data) $this.data('decapitate', new Decapitate(this, options));
            if (typeof option === 'string') data[option]();
        });
    };

    $.fn.decapitate.Constructor = Decapitate;

    $.fn.decapitate.defaults = {};

    $.fn.decapitate.noConflict = function() {
        $.fn.decapitate = old;
        return this;
    };

})(window.jQuery);
