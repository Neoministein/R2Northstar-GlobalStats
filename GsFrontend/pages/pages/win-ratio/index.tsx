import { Column } from 'primereact/column';
import BackendService from '../../../demo/service/BackendService';
import { WinRatioBucket } from '../../../demo/service/BackendService';
import TopResultTable from '../../../demo/components/TopResultTable';

const ButtonDemo = () => {

    const winRatioBody = (column: WinRatioBucket) => {
        return column.ratio + "%"
    }

    return (
        <TopResultTable 
            title="Top Player Kills" 
            getGlobalRanking={() => BackendService.getTopWinRatio()}
            columns={
                [<Column header="Player Name" field="playerName" />,
                <Column header="Ratio" body={winRatioBody} />,
                <Column header="Wins"  field="filters.win" />,
                <Column header="Loses" field="filters.lose" />]}/> 
    );
};

export default ButtonDemo;